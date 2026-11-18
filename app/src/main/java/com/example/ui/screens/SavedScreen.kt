package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.DormBiteUiState
import com.example.ui.components.RecipeCard

@Composable
fun SavedScreen(
    uiState: DormBiteUiState,
    onRecipeClick: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onToggleGroceryChecked: (String) -> Unit,
    onRemoveGroceryItem: (String) -> Unit,
    onClearCheckedGrocery: () -> Unit,
    onAddCustomGroceryItem: (name: String, amount: String, cost: Double) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var showAddDialog by remember { mutableStateOf(false) }

    if (showAddDialog) {
        AddGroceryItemDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, amount, cost ->
                onAddCustomGroceryItem(name, amount, cost)
                showAddDialog = false
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Saved & Budget List",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            if (selectedTab == 1) {
                FilledTonalButton(
                    onClick = { showAddDialog = true },
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("add_custom_grocery_button")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Item")
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            SegmentedButton(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                modifier = Modifier.testTag("saved_tab_recipes")
            ) {
                Text("${stringResource(R.string.saved_meals_tab)} (${uiState.favoriteRecipes.size})")
            }
            SegmentedButton(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                modifier = Modifier.testTag("saved_tab_grocery")
            ) {
                Text("${stringResource(R.string.grocery_list_tab)} (${uiState.groceryItems.size})")
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (selectedTab == 0) {
            if (uiState.favoriteRecipes.isEmpty()) {
                SavedEmptyCard(
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(44.dp)
                        )
                    },
                    title = stringResource(R.string.empty_saved_title),
                    subtitle = stringResource(R.string.empty_saved_subtitle)
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = uiState.favoriteRecipes,
                        key = { it.id }
                    ) { recipe ->
                        RecipeCard(
                            recipe = recipe,
                            isFavorite = true,
                            onRecipeClick = { onRecipeClick(recipe.id) },
                            onFavoriteClick = { onToggleFavorite(recipe.id) }
                        )
                    }
                }
            }
        } else {
            if (uiState.groceryItems.isEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SavedEmptyCard(
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.ShoppingBag,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(44.dp)
                            )
                        },
                        title = stringResource(R.string.empty_grocery_title),
                        subtitle = stringResource(R.string.empty_grocery_subtitle)
                    )
                    Button(
                        onClick = { showAddDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .minimumInteractiveComponentSize()
                            .testTag("empty_add_grocery_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Add Budget Grocery Item")
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "Estimated Remaining Trip",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                    Text(
                                        text = "Total list: $${String.format("%.2f", uiState.totalGroceryTotal)}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                Text(
                                    text = String.format("$%.2f", uiState.remainingGroceryTotal),
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontFamily = MaterialTheme.typography.labelSmall.fontFamily
                                    ),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    if (uiState.groceryItems.any { it.isChecked }) {
                        item {
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextButton(
                                    onClick = onClearCheckedGrocery,
                                    modifier = Modifier
                                        .minimumInteractiveComponentSize()
                                        .testTag("clear_checked_grocery_button")
                                ) {
                                    Text("Clear Checked Items")
                                }
                            }
                        }
                    }

                    item {
                        Card(
                            shape = MaterialTheme.shapes.medium,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                uiState.groceryItems.forEachIndexed { index, item ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { onToggleGroceryChecked(item.key) }
                                            .padding(horizontal = 8.dp, vertical = 6.dp)
                                            .testTag("grocery_item_${item.key}")
                                    ) {
                                        Checkbox(
                                            checked = item.isChecked,
                                            onCheckedChange = { onToggleGroceryChecked(item.key) },
                                            modifier = Modifier.minimumInteractiveComponentSize()
                                        )
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = item.name,
                                                style = MaterialTheme.typography.bodyLarge,
                                                textDecoration = if (item.isChecked) TextDecoration.LineThrough else TextDecoration.None,
                                                color = if (item.isChecked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = item.amountDisplay,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                        Text(
                                            text = String.format("$%.2f", item.estimatedCost),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        IconButton(
                                            onClick = { onRemoveGroceryItem(item.key) },
                                            modifier = Modifier.minimumInteractiveComponentSize()
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.DeleteOutline,
                                                contentDescription = "Remove ${item.name}",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                    if (index < uiState.groceryItems.lastIndex) {
                                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SavedEmptyCard(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(28.dp)
    ) {
        icon()
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun AddGroceryItemDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, amount: String, cost: Double) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Grocery Item") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Item name (e.g., Bananas)") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_grocery_name_input")
                )
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Quantity (optional, e.g., 1 bunch)") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_grocery_amount_input")
                )
                OutlinedTextField(
                    value = priceText,
                    onValueChange = { input ->
                        if (input.isEmpty() || input.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
                            priceText = input
                        }
                    },
                    label = { Text("Est. price USD (e.g., 1.25)") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_grocery_price_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val cleanName = name.trim()
                    if (cleanName.isNotEmpty()) {
                        val cost = priceText.toDoubleOrNull() ?: 0.0
                        onConfirm(cleanName, amount.trim(), cost)
                    }
                },
                enabled = name.trim().isNotEmpty(),
                modifier = Modifier.testTag("confirm_add_grocery_button")
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

