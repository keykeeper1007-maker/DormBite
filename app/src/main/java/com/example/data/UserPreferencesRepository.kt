package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dormBiteDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "dormbite_prefs"
)

class UserPreferencesRepository(private val context: Context) {

    private object Keys {
        val FAVORITES = stringSetPreferencesKey("favorite_recipe_ids")
        val PANTRY_STAPLES = stringSetPreferencesKey("pantry_staple_keys")
        val GROCERY_ITEMS = stringSetPreferencesKey("grocery_items_encoded")
    }

    val favoriteRecipeIds: Flow<Set<String>> = context.dormBiteDataStore.data.map { prefs ->
        prefs[Keys.FAVORITES] ?: emptySet()
    }

    val pantryStapleKeys: Flow<Set<String>> = context.dormBiteDataStore.data.map { prefs ->
        prefs[Keys.PANTRY_STAPLES] ?: setOf("eggs", "oats", "peanut_butter", "spinach")
    }

    val groceryItems: Flow<List<GroceryItem>> = context.dormBiteDataStore.data.map { prefs ->
        val rawSet = prefs[Keys.GROCERY_ITEMS] ?: emptySet()
        rawSet.mapNotNull { decodeGroceryItem(it) }
            .sortedWith(compareBy<GroceryItem> { it.isChecked }.thenBy { it.name })
    }

    suspend fun toggleFavorite(recipeId: String) {
        context.dormBiteDataStore.edit { prefs ->
            val current = prefs[Keys.FAVORITES]?.toMutableSet() ?: mutableSetOf()
            if (!current.add(recipeId)) {
                current.remove(recipeId)
            }
            prefs[Keys.FAVORITES] = current
        }
    }

    suspend fun togglePantryStaple(key: String) {
        context.dormBiteDataStore.edit { prefs ->
            val current = (prefs[Keys.PANTRY_STAPLES] ?: setOf("eggs", "oats", "peanut_butter", "spinach")).toMutableSet()
            if (!current.add(key)) {
                current.remove(key)
            }
            prefs[Keys.PANTRY_STAPLES] = current
        }
    }

    suspend fun setPantryPreset(keys: Set<String>) {
        context.dormBiteDataStore.edit { prefs ->
            prefs[Keys.PANTRY_STAPLES] = keys
        }
    }

    suspend fun addGroceryItems(items: List<GroceryItem>) {
        context.dormBiteDataStore.edit { prefs ->
            val currentMap = (prefs[Keys.GROCERY_ITEMS] ?: emptySet())
                .mapNotNull { decodeGroceryItem(it) }
                .associateBy { it.key }
                .toMutableMap()

            for (item in items) {
                if (!currentMap.containsKey(item.key)) {
                    currentMap[item.key] = item
                }
            }
            prefs[Keys.GROCERY_ITEMS] = currentMap.values.map { encodeGroceryItem(it) }.toSet()
        }
    }

    suspend fun toggleGroceryChecked(key: String) {
        context.dormBiteDataStore.edit { prefs ->
            val updated = (prefs[Keys.GROCERY_ITEMS] ?: emptySet())
                .mapNotNull { decodeGroceryItem(it) }
                .map { item ->
                    if (item.key == key) item.copy(isChecked = !item.isChecked) else item
                }
                .map { encodeGroceryItem(it) }
                .toSet()
            prefs[Keys.GROCERY_ITEMS] = updated
        }
    }

    suspend fun clearCheckedGroceryItems() {
        context.dormBiteDataStore.edit { prefs ->
            val remaining = (prefs[Keys.GROCERY_ITEMS] ?: emptySet())
                .mapNotNull { decodeGroceryItem(it) }
                .filterNot { it.isChecked }
                .map { encodeGroceryItem(it) }
                .toSet()
            prefs[Keys.GROCERY_ITEMS] = remaining
        }
    }

    suspend fun removeGroceryItem(key: String) {
        context.dormBiteDataStore.edit { prefs ->
            val remaining = (prefs[Keys.GROCERY_ITEMS] ?: emptySet())
                .mapNotNull { decodeGroceryItem(it) }
                .filterNot { it.key == key }
                .map { encodeGroceryItem(it) }
                .toSet()
            prefs[Keys.GROCERY_ITEMS] = remaining
        }
    }

    private fun encodeGroceryItem(item: GroceryItem): String {
        val safeName = item.name.replace("|", "/")
        val safeAmount = item.amountDisplay.replace("|", "/")
        return listOf(
            item.key,
            safeName,
            safeAmount,
            item.estimatedCost.toString(),
            item.isChecked.toString()
        ).joinToString("|")
    }

    private fun decodeGroceryItem(raw: String): GroceryItem? {
        val parts = raw.split("|")
        if (parts.size < 5) return null
        val cost = parts[3].toDoubleOrNull() ?: return null
        val checked = parts[4].toBooleanStrictOrNull() ?: false
        return GroceryItem(
            key = parts[0],
            name = parts[1],
            amountDisplay = parts[2],
            estimatedCost = cost,
            isChecked = checked
        )
    }
}
