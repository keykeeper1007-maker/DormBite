package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.EquipmentType
import com.example.data.GroceryItem
import com.example.data.MealFilter
import com.example.data.PantryStaple
import com.example.data.Recipe
import com.example.data.RecipeCatalog
import com.example.data.RecipeMatchResult
import com.example.data.UserPreferencesRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TimerUiState(
    val recipeId: String = "",
    val recipeTitle: String = "",
    val label: String = "",
    val totalSeconds: Int = 0,
    val remainingSeconds: Int = 0,
    val isRunning: Boolean = false,
    val isFinished: Boolean = false
) {
    val isActive: Boolean get() = totalSeconds > 0
    val progress: Float
        get() = if (totalSeconds > 0) {
            1f - (remainingSeconds.toFloat() / totalSeconds.toFloat())
        } else 0f

    fun formattedRemaining(): String {
        val mins = remainingSeconds / 60
        val secs = remainingSeconds % 60
        return String.format("%02d:%02d", mins, secs)
    }
}

data class DormBiteUiState(
    val searchQuery: String = "",
    val selectedEquipment: EquipmentType? = null,
    val selectedMealFilter: MealFilter = MealFilter.ALL,
    val featuredRecipe: Recipe = RecipeCatalog.recipes.first(),
    val filteredRecipes: List<Recipe> = RecipeCatalog.recipes,
    val favoriteIds: Set<String> = emptySet(),
    val favoriteRecipes: List<Recipe> = emptyList(),
    val pantryStaples: List<PantryStaple> = RecipeCatalog.pantryStaples,
    val pantryStapleKeys: Set<String> = emptySet(),
    val recipeMatches: List<RecipeMatchResult> = emptyList(),
    val groceryItems: List<GroceryItem> = emptyList(),
    val timerState: TimerUiState = TimerUiState(),
    val servings: Int = 1,
    val snackbarMessage: String? = null
) {
    val readyNowCount: Int
        get() = recipeMatches.count { it.isReadyNow }

    val remainingGroceryTotal: Double
        get() = groceryItems.filterNot { it.isChecked }.sumOf { it.estimatedCost }

    val totalGroceryTotal: Double
        get() = groceryItems.sumOf { it.estimatedCost }
}

class DormBiteViewModel(
    private val repository: UserPreferencesRepository
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val selectedEquipment = MutableStateFlow<EquipmentType?>(null)
    private val selectedMealFilter = MutableStateFlow(MealFilter.ALL)
    private val timerState = MutableStateFlow(TimerUiState())
    private val servings = MutableStateFlow(1)
    private val snackbarMessage = MutableStateFlow<String?>(null)

    private var timerJob: Job? = null

    private data class FilterInputs(
        val query: String,
        val equipment: EquipmentType?,
        val mealFilter: MealFilter,
        val servings: Int,
        val timer: TimerUiState,
        val snackbar: String?
    )

    private val filterInputs = combine(
        searchQuery,
        selectedEquipment,
        selectedMealFilter,
        servings,
        timerState,
        snackbarMessage
    ) { values ->
        FilterInputs(
            query = values[0] as String,
            equipment = values[1] as EquipmentType?,
            mealFilter = values[2] as MealFilter,
            servings = values[3] as Int,
            timer = values[4] as TimerUiState,
            snackbar = values[5] as String?
        )
    }

    val uiState: StateFlow<DormBiteUiState> = combine(
        filterInputs,
        repository.favoriteRecipeIds,
        repository.pantryStapleKeys,
        repository.groceryItems
    ) { inputs, favIds, pantryKeys, groceryList ->
        val queryTrimmed = inputs.query.trim().lowercase()

        val filtered = RecipeCatalog.recipes.filter { recipe ->
            val matchesEquipment = inputs.equipment == null || recipe.equipment == inputs.equipment
            val matchesMealFilter = when (inputs.mealFilter) {
                MealFilter.ALL -> true
                MealFilter.UNDER_2_DOLLARS -> recipe.costPerServing <= 2.00
                MealFilter.HIGH_PROTEIN -> recipe.proteinGrams >= 20
                MealFilter.VEGETARIAN -> recipe.isVegetarian
                MealFilter.BREAKFAST -> recipe.isBreakfast
                MealFilter.LATE_NIGHT -> recipe.isLateNight
            }
            val matchesQuery = queryTrimmed.isEmpty() ||
                recipe.title.lowercase().contains(queryTrimmed) ||
                recipe.tagline.lowercase().contains(queryTrimmed) ||
                recipe.ingredients.any { it.name.lowercase().contains(queryTrimmed) }
            matchesEquipment && matchesMealFilter && matchesQuery
        }

        val matches = RecipeCatalog.recipes.map { recipe ->
            val trackable = recipe.ingredients.filter { it.pantryStapleKey != null }
            val missing = trackable.filter { !pantryKeys.contains(it.pantryStapleKey) }
            RecipeMatchResult(
                recipe = recipe,
                matchedCount = trackable.size - missing.size,
                totalTrackableCount = trackable.size,
                missingIngredients = missing
            )
        }.sortedWith(
            compareBy<RecipeMatchResult> { it.missingIngredients.size }
                .thenBy { it.recipe.prepMinutes }
                .thenBy { it.recipe.costPerServing }
        )

        val favRecipes = RecipeCatalog.recipes.filter { favIds.contains(it.id) }

        DormBiteUiState(
            searchQuery = inputs.query,
            selectedEquipment = inputs.equipment,
            selectedMealFilter = inputs.mealFilter,
            featuredRecipe = RecipeCatalog.recipes.first(),
            filteredRecipes = filtered,
            favoriteIds = favIds,
            favoriteRecipes = favRecipes,
            pantryStaples = RecipeCatalog.pantryStaples,
            pantryStapleKeys = pantryKeys,
            recipeMatches = matches,
            groceryItems = groceryList,
            timerState = inputs.timer,
            servings = inputs.servings,
            snackbarMessage = inputs.snackbar
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DormBiteUiState()
    )

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun selectEquipment(equipment: EquipmentType?) {
        selectedEquipment.value = if (selectedEquipment.value == equipment) null else equipment
    }

    fun selectMealFilter(filter: MealFilter) {
        selectedMealFilter.value = filter
    }

    fun clearFilters() {
        searchQuery.value = ""
        selectedEquipment.value = null
        selectedMealFilter.value = MealFilter.ALL
    }

    fun toggleFavorite(recipeId: String) {
        viewModelScope.launch {
            repository.toggleFavorite(recipeId)
        }
    }

    fun togglePantryStaple(key: String) {
        viewModelScope.launch {
            repository.togglePantryStaple(key)
        }
    }

    fun applyStarterDormKit() {
        viewModelScope.launch {
            repository.setPantryPreset(
                setOf("eggs", "oats", "peanut_butter", "spinach", "black_beans", "rice_cup", "tortilla", "banana")
            )
            snackbarMessage.value = "Loaded starter dorm kit (8 staples)"
        }
    }

    fun clearPantry() {
        viewModelScope.launch {
            repository.setPantryPreset(emptySet())
        }
    }

    fun setServings(count: Int) {
        servings.value = count.coerceIn(1, 3)
    }

    fun addMissingToGroceryList(recipe: Recipe) {
        val currentPantry = uiState.value.pantryStapleKeys
        val mult = servings.value
        val missing = recipe.ingredients.filter { item ->
            item.pantryStapleKey == null || !currentPantry.contains(item.pantryStapleKey)
        }
        val toAdd = if (missing.isEmpty()) recipe.ingredients else missing
        val groceryItems = toAdd.map { ing ->
            GroceryItem(
                key = ing.pantryStapleKey ?: ing.id,
                name = ing.name,
                amountDisplay = ing.formatQuantity(mult),
                estimatedCost = ing.estimatedCostUsd * mult,
                isChecked = false
            )
        }
        viewModelScope.launch {
            repository.addGroceryItems(groceryItems)
            snackbarMessage.value = if (missing.isEmpty()) {
                "Added all ${groceryItems.size} ingredients to Grocery List"
            } else {
                "Added ${groceryItems.size} missing items to Grocery List"
            }
        }
    }

    fun addSingleIngredientToGroceryList(name: String, amountDisplay: String, cost: Double, key: String) {
        viewModelScope.launch {
            repository.addGroceryItems(
                listOf(
                    GroceryItem(
                        key = key,
                        name = name,
                        amountDisplay = amountDisplay,
                        estimatedCost = cost,
                        isChecked = false
                    )
                )
            )
            snackbarMessage.value = "Added $name to Grocery List"
        }
    }

    fun toggleGroceryChecked(key: String) {
        viewModelScope.launch {
            repository.toggleGroceryChecked(key)
        }
    }

    fun clearCheckedGroceryItems() {
        viewModelScope.launch {
            repository.clearCheckedGroceryItems()
        }
    }

    fun removeGroceryItem(key: String) {
        viewModelScope.launch {
            repository.removeGroceryItem(key)
        }
    }

    fun startTimer(recipe: Recipe, label: String, durationSeconds: Int) {
        timerJob?.cancel()
        timerState.value = TimerUiState(
            recipeId = recipe.id,
            recipeTitle = recipe.title,
            label = label,
            totalSeconds = durationSeconds,
            remainingSeconds = durationSeconds,
            isRunning = true,
            isFinished = false
        )
        launchTicker()
    }

    fun toggleTimerRunning() {
        val current = timerState.value
        if (!current.isActive) return
        if (current.isFinished) {
            timerState.value = current.copy(
                remainingSeconds = current.totalSeconds,
                isRunning = true,
                isFinished = false
            )
            launchTicker()
        } else if (current.isRunning) {
            timerJob?.cancel()
            timerState.value = current.copy(isRunning = false)
        } else {
            timerState.value = current.copy(isRunning = true)
            launchTicker()
        }
    }

    fun resetTimer() {
        timerJob?.cancel()
        val current = timerState.value
        if (current.isActive) {
            timerState.value = current.copy(
                remainingSeconds = current.totalSeconds,
                isRunning = false,
                isFinished = false
            )
        }
    }

    fun dismissTimer() {
        timerJob?.cancel()
        timerState.value = TimerUiState()
    }

    fun consumeSnackbar() {
        snackbarMessage.value = null
    }

    fun getRecipeById(id: String): Recipe? = RecipeCatalog.recipes.find { it.id == id }

    private fun launchTicker() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (timerState.value.isRunning && timerState.value.remainingSeconds > 0) {
                delay(1_000L)
                val latest = timerState.value
                if (!latest.isRunning) break
                val next = (latest.remainingSeconds - 1).coerceAtLeast(0)
                timerState.value = latest.copy(
                    remainingSeconds = next,
                    isRunning = next > 0,
                    isFinished = next == 0
                )
            }
        }
    }

    class Factory(private val repository: UserPreferencesRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DormBiteViewModel(repository) as T
        }
    }
}
