package com.example.data

import androidx.annotation.DrawableRes

enum class EquipmentType(val label: String) {
    MICROWAVE("Microwave"),
    NO_COOK("No-Cook"),
    ONE_PAN("1-Pan / Kettle")
}

enum class MealFilter(val label: String) {
    ALL("All Meals"),
    UNDER_2_DOLLARS("Under $2"),
    HIGH_PROTEIN("20g+ Protein"),
    VEGETARIAN("Vegetarian"),
    BREAKFAST("Breakfast"),
    LATE_NIGHT("Late-Night")
}

enum class StapleGroup(val label: String) {
    FRIDGE("Mini-Fridge"),
    PANTRY("Dry Pantry"),
    PRODUCE("Quick Fresh")
}

data class PantryStaple(
    val key: String,
    val name: String,
    val group: StapleGroup,
    val typicalPriceNote: String
)

data class IngredientItem(
    val id: String,
    val name: String,
    val amount: Double,
    val unit: String,
    val estimatedCostUsd: Double,
    val pantryStapleKey: String? = null
) {
    fun formatQuantity(servings: Int): String {
        val scaled = amount * servings
        val numStr = when {
            scaled % 1.0 == 0.0 -> scaled.toInt().toString()
            (scaled * 2) % 1.0 == 0.0 -> {
                val whole = scaled.toInt()
                if (whole > 0) "$whole½" else "½"
            }
            (scaled * 4) % 1.0 == 0.0 -> {
                val quarters = (scaled * 4).toInt()
                when (quarters) {
                    1 -> "¼"
                    3 -> "¾"
                    else -> String.format("%.2f", scaled)
                }
            }
            else -> String.format("%.1f", scaled)
        }
        return if (unit.isBlank()) numStr else "$numStr $unit"
    }
}

data class CookingStep(
    val stepNumber: Int,
    val title: String,
    val instruction: String,
    val durationSeconds: Int? = null
)

data class Recipe(
    val id: String,
    val title: String,
    val tagline: String,
    val prepMinutes: Int,
    val costPerServing: Double,
    val calories: Int,
    val proteinGrams: Int,
    val fiberGrams: Int,
    val equipment: EquipmentType,
    val isVegetarian: Boolean,
    val isBreakfast: Boolean,
    val isLateNight: Boolean,
    @DrawableRes val imageResId: Int,
    val ingredients: List<IngredientItem>,
    val steps: List<CookingStep>,
    val dormHack: String
)

data class GroceryItem(
    val key: String,
    val name: String,
    val amountDisplay: String,
    val estimatedCost: Double,
    val isChecked: Boolean = false
)

data class RecipeMatchResult(
    val recipe: Recipe,
    val matchedCount: Int,
    val totalTrackableCount: Int,
    val missingIngredients: List<IngredientItem>
) {
    val missingCost: Double
        get() = missingIngredients.sumOf { it.estimatedCostUsd }

    val isReadyNow: Boolean
        get() = missingIngredients.isEmpty()
}
