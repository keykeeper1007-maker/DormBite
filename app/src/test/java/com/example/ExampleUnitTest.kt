package com.example

import com.example.data.IngredientItem
import com.example.data.RecipeCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun allRecipesAreUnderTenMinutesAndBudgetFriendly() {
        assertTrue(RecipeCatalog.recipes.isNotEmpty())
        for (recipe in RecipeCatalog.recipes) {
            assertTrue("${recipe.id} should be <= 10 min", recipe.prepMinutes in 1..10)
            assertTrue("${recipe.id} cost should be reasonable", recipe.costPerServing > 0.0 && recipe.costPerServing <= 4.0)
            assertTrue("${recipe.id} needs ingredients", recipe.ingredients.isNotEmpty())
            assertTrue("${recipe.id} needs steps", recipe.steps.isNotEmpty())
        }
    }

    @Test
    fun ingredientQuantityScalesCleanly() {
        val halfCup = IngredientItem("id", "Oats", 0.5, "cup", 0.25)
        assertEquals("½ cup", halfCup.formatQuantity(1))
        assertEquals("1 cup", halfCup.formatQuantity(2))
    }
}
