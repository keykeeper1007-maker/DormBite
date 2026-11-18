package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Kitchen
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.StapleGroup
import com.example.ui.DormBiteUiState
import com.example.ui.components.RecipeCard

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FridgeMatchScreen(
    uiState: DormBiteUiState,
    onToggleStaple: (String) -> Unit,
    onApplyStarterKit: () -> Unit,
    onClearPantry: () -> Unit,
    onRecipeClick: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isWide = maxWidth >= 720.dp

        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .testTag("fridge_match_list")
        ) {
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.pantry_title),
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(R.string.pantry_subtitle),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "${uiState.readyNowCount}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "ready",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AssistChip(
                            onClick = onApplyStarterKit,
                            label = { Text("Starter Dorm Kit") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.AutoAwesome,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("apply_starter_kit_button")
                        )
                        TextButton(
                            onClick = onClearPantry,
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("clear_pantry_button")
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.RestartAlt,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Reset (${uiState.pantryStapleKeys.size})")
                        }
                    }
                }
            }

            // Staple Selector Card
            item {
                Card(
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        StapleGroup.entries.forEach { group ->
                            val groupItems = uiState.pantryStaples.filter { it.group == group }
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Outlined.Kitchen,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = group.label,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    groupItems.forEach { staple ->
                                        val checked = uiState.pantryStapleKeys.contains(staple.key)
                                        FilterChip(
                                            selected = checked,
                                            onClick = { onToggleStaple(staple.key) },
                                            label = { Text(staple.name) },
                                            leadingIcon = if (checked) {
                                                {
                                                    Icon(
                                                        imageVector = Icons.Filled.Check,
                                                        contentDescription = null,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                            } else null,
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                                selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer
                                            ),
                                            modifier = Modifier
                                                .minimumInteractiveComponentSize()
                                                .testTag("staple_chip_${staple.key}")
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Ranked by What You Have",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            if (isWide) {
                val chunked = uiState.recipeMatches.chunked(2)
                items(chunked) { pair ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        pair.forEach { match ->
                            val badge = buildMatchBadge(match.isReadyNow, match.missingIngredients.size, match.missingCost)
                            RecipeCard(
                                recipe = match.recipe,
                                isFavorite = uiState.favoriteIds.contains(match.recipe.id),
                                onRecipeClick = { onRecipeClick(match.recipe.id) },
                                onFavoriteClick = { onToggleFavorite(match.recipe.id) },
                                badgeNote = badge,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (pair.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            } else {
                items(
                    items = uiState.recipeMatches,
                    key = { it.recipe.id }
                ) { match ->
                    val badge = buildMatchBadge(match.isReadyNow, match.missingIngredients.size, match.missingCost)
                    RecipeCard(
                        recipe = match.recipe,
                        isFavorite = uiState.favoriteIds.contains(match.recipe.id),
                        onRecipeClick = { onRecipeClick(match.recipe.id) },
                        onFavoriteClick = { onToggleFavorite(match.recipe.id) },
                        badgeNote = badge
                    )
                }
            }
        }
    }
}

private fun buildMatchBadge(readyNow: Boolean, missingCount: Int, missingCost: Double): String {
    return if (readyNow) {
        "Ready to make right now!"
    } else if (missingCount == 1) {
        "Missing 1 item (~$${String.format("%.2f", missingCost)})"
    } else {
        "Missing $missingCount items (~$${String.format("%.2f", missingCost)})"
    }
}
