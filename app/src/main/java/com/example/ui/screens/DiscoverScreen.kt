package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.FilterListOff
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.EquipmentType
import com.example.data.MealFilter
import com.example.data.Recipe
import com.example.ui.DormBiteUiState
import com.example.ui.components.AboutPrivacyDialog
import com.example.ui.components.RecipeCard
import com.example.ui.components.icon

@Composable
fun DiscoverScreen(
    uiState: DormBiteUiState,
    onSearchChange: (String) -> Unit,
    onSelectEquipment: (EquipmentType?) -> Unit,
    onSelectMealFilter: (MealFilter) -> Unit,
    onClearFilters: () -> Unit,
    onRecipeClick: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAboutDialog by remember { mutableStateOf(false) }

    if (showAboutDialog) {
        AboutPrivacyDialog(onDismiss = { showAboutDialog = false })
    }

    val showHero = uiState.searchQuery.isBlank() &&
        uiState.selectedEquipment == null &&
        uiState.selectedMealFilter == MealFilter.ALL

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isWide = maxWidth >= 640.dp
        val columns = if (isWide) 2 else 1

        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .fillMaxSize()
                .testTag("discover_recipe_list")
        ) {
            // Header
            item(span = { GridItemSpan(columns) }) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "DormBite",
                                style = MaterialTheme.typography.displaySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Healthy meals under 10 min & $3",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Surface(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                shape = RoundedCornerShape(50)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Timer,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = "≤10 min",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            IconButton(
                                onClick = { showAboutDialog = true },
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .testTag("about_privacy_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Info,
                                    contentDescription = "About DormBite and privacy",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = onSearchChange,
                        placeholder = {
                            Text(
                                text = stringResource(R.string.search_placeholder),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = "Search recipes"
                            )
                        },
                        trailingIcon = {
                            if (uiState.searchQuery.isNotEmpty()) {
                                IconButton(
                                    onClick = { onSearchChange("") },
                                    modifier = Modifier
                                        .minimumInteractiveComponentSize()
                                        .testTag("clear_search_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "Clear search"
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("recipe_search_input")
                    )
                }
            }

            // Equipment & Meal Filters
            item(span = { GridItemSpan(columns) }) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(EquipmentType.entries) { equip ->
                            val selected = uiState.selectedEquipment == equip
                            FilterChip(
                                selected = selected,
                                onClick = { onSelectEquipment(equip) },
                                label = { Text(equip.label) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = equip.icon(),
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                    selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .testTag("equipment_chip_${equip.name.lowercase()}")
                            )
                        }
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(MealFilter.entries) { filter ->
                            val selected = uiState.selectedMealFilter == filter
                            FilterChip(
                                selected = selected,
                                onClick = { onSelectMealFilter(filter) },
                                label = { Text(filter.label) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
                                ),
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .testTag("meal_filter_chip_${filter.name.lowercase()}")
                            )
                        }
                    }
                }
            }

            // Hero Featured Pick
            if (showHero) {
                item(span = { GridItemSpan(columns) }) {
                    HeroFeaturedPickCard(
                        recipe = uiState.featuredRecipe,
                        onCookNowClick = { onRecipeClick(uiState.featuredRecipe.id) }
                    )
                }
            }

            // Section Title
            item(span = { GridItemSpan(columns) }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (showHero) "All 10-Min Recipes" else "${uiState.filteredRecipes.size} Matching Meals",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    if (!showHero) {
                        OutlinedButton(
                            onClick = onClearFilters,
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("clear_filters_button")
                        ) {
                            Text(stringResource(R.string.action_clear_filters))
                        }
                    }
                }
            }

            if (uiState.filteredRecipes.isEmpty()) {
                item(span = { GridItemSpan(columns) }) {
                    EmptyDiscoverState(onClearFilters = onClearFilters)
                }
            } else {
                items(
                    items = uiState.filteredRecipes,
                    key = { it.id }
                ) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        isFavorite = uiState.favoriteIds.contains(recipe.id),
                        onRecipeClick = { onRecipeClick(recipe.id) },
                        onFavoriteClick = { onToggleFavorite(recipe.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HeroFeaturedPickCard(
    recipe: Recipe,
    onCookNowClick: () -> Unit
) {
    Card(
        onClick = onCookNowClick,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("hero_featured_card")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(228.dp)
        ) {
            Image(
                painter = painterResource(id = recipe.imageResId),
                contentDescription = recipe.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.25f),
                                Color.Black.copy(alpha = 0.78f)
                            )
                        )
                    )
            )

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    shape = RoundedCornerShape(50)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocalFireDepartment,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.hero_badge),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Column {
                    Text(
                        text = recipe.title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "${recipe.prepMinutes} min • $${String.format("%.2f", recipe.costPerServing)} • ${recipe.proteinGrams}g protein",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White.copy(alpha = 0.92f)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onCookNowClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("hero_cook_now_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Bolt,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = stringResource(R.string.cook_now, recipe.prepMinutes))
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyDiscoverState(
    onClearFilters: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(28.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.FilterListOff,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(44.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.empty_recipes_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(R.string.empty_recipes_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onClearFilters,
            modifier = Modifier
                .minimumInteractiveComponentSize()
                .testTag("empty_clear_filters_button")
        ) {
            Text(stringResource(R.string.action_clear_filters))
        }
    }
}
