package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.AddShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.Recipe
import com.example.ui.DormBiteUiState
import com.example.ui.components.icon

@Composable
fun RecipeDetailScreen(
    recipe: Recipe,
    uiState: DormBiteUiState,
    onBackClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onServingsChange: (Int) -> Unit,
    onAddMissingToGrocery: () -> Unit,
    onStartTimer: (label: String, seconds: Int) -> Unit,
    onToggleTimerPause: () -> Unit,
    onResetTimer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isFavorite = uiState.favoriteIds.contains(recipe.id)
    val servings = uiState.servings
    var checkedIngredients by remember(recipe.id) { mutableStateOf(emptySet<String>()) }
    val isThisRecipeTimer = uiState.timerState.isActive && uiState.timerState.recipeId == recipe.id

    LazyColumn(
        contentPadding = PaddingValues(bottom = 32.dp),
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
            .testTag("recipe_detail_screen")
    ) {
        // Hero Photo Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(290.dp)
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
                                    Color.Black.copy(alpha = 0.48f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.72f)
                                )
                            )
                        )
                )

                // Floating Top Controls inside Status Bar Insets
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.90f)
                    ) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("detail_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.action_back)
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.90f)
                        ) {
                            IconButton(
                                onClick = {
                                    val shareText = buildString {
                                        appendLine("${recipe.title} (${recipe.prepMinutes} min • $${String.format("%.2f", recipe.costPerServing)}/serving)")
                                        appendLine(recipe.tagline)
                                        appendLine()
                                        appendLine("Ingredients (${servings}x):")
                                        recipe.ingredients.forEach {
                                            appendLine("- ${it.formatQuantity(servings)} ${it.name}")
                                        }
                                        appendLine()
                                        appendLine("Steps:")
                                        recipe.steps.forEach {
                                            appendLine("${it.stepNumber}. ${it.title}: ${it.instruction}")
                                        }
                                    }
                                    val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_SUBJECT, recipe.title)
                                        putExtra(Intent.EXTRA_TEXT, shareText)
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, "Share recipe"))
                                },
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .testTag("detail_share_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Share,
                                    contentDescription = "Share recipe"
                                )
                            }
                        }

                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.90f)
                        ) {
                            IconButton(
                                onClick = onToggleFavorite,
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .testTag("detail_favorite_button")
                            ) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = stringResource(R.string.action_favorite),
                                    tint = if (isFavorite) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                // Bottom Overlay Title
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(18.dp)
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                        shape = RoundedCornerShape(50)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = recipe.equipment.icon(),
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = recipe.equipment.label,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = recipe.title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                }
            }
        }

        // Quick Stats Strip + Full Meal Timer Card
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text(
                    text = recipe.tagline,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StatPill(
                        label = "Time",
                        value = "${recipe.prepMinutes} min",
                        modifier = Modifier.weight(1f)
                    )
                    StatPill(
                        label = "Est. Cost",
                        value = String.format("$%.2f", recipe.costPerServing * servings),
                        modifier = Modifier.weight(1f)
                    )
                    StatPill(
                        label = "Protein",
                        value = "${recipe.proteinGrams * servings}g",
                        modifier = Modifier.weight(1f)
                    )
                    StatPill(
                        label = "Calories",
                        value = "${recipe.calories * servings}",
                        modifier = Modifier.weight(1f)
                    )
                }

                // Live Cook Timer Box
                Card(
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isThisRecipeTimer) uiState.timerState.label else "Total Meal Timer",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = if (isThisRecipeTimer) {
                                    if (uiState.timerState.isFinished) "Time's up! Enjoy your meal." else uiState.timerState.formattedRemaining()
                                } else {
                                    String.format("%02d:00 countdown", recipe.prepMinutes)
                                },
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontFamily = MaterialTheme.typography.labelSmall.fontFamily
                                ),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (isThisRecipeTimer) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                FilledTonalButton(
                                    onClick = onToggleTimerPause,
                                    modifier = Modifier
                                        .minimumInteractiveComponentSize()
                                        .testTag("detail_timer_toggle")
                                ) {
                                    Icon(
                                        imageVector = if (uiState.timerState.isRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                        contentDescription = if (uiState.timerState.isRunning) "Pause" else "Resume"
                                    )
                                }
                                IconButton(
                                    onClick = onResetTimer,
                                    modifier = Modifier
                                        .minimumInteractiveComponentSize()
                                        .testTag("detail_timer_reset")
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Refresh,
                                        contentDescription = "Reset timer",
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        } else {
                            Button(
                                onClick = {
                                    onStartTimer(
                                        "${recipe.prepMinutes}-Min Meal Timer",
                                        recipe.prepMinutes * 60
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .testTag("start_full_timer_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Timer,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Start")
                            }
                        }
                    }
                }
            }
        }

        // Ingredients + Servings Scaler
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Ingredients",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    SingleChoiceSegmentedButtonRow {
                        listOf(1, 2).forEachIndexed { index, count ->
                            SegmentedButton(
                                selected = servings == count,
                                onClick = { onServingsChange(count) },
                                shape = SegmentedButtonDefaults.itemShape(index = index, count = 2),
                                modifier = Modifier.testTag("servings_${count}x")
                            ) {
                                Text("${count}x")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        recipe.ingredients.forEachIndexed { index, ing ->
                            val isChecked = checkedIngredients.contains(ing.id)
                            val inPantry = ing.pantryStapleKey != null &&
                                uiState.pantryStapleKeys.contains(ing.pantryStapleKey)

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        checkedIngredients = if (isChecked) {
                                            checkedIngredients - ing.id
                                        } else {
                                            checkedIngredients + ing.id
                                        }
                                    }
                                    .padding(horizontal = 14.dp, vertical = 12.dp)
                                    .testTag("ingredient_row_${ing.id}")
                            ) {
                                Icon(
                                    imageVector = if (isChecked) Icons.Filled.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                                    contentDescription = null,
                                    tint = if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = ing.name,
                                        style = MaterialTheme.typography.bodyLarge,
                                        textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None,
                                        color = if (isChecked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                                    )
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Text(
                                            text = ing.formatQuantity(servings),
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        if (inPantry) {
                                            Text(
                                                text = "• In your pantry",
                                                style = MaterialTheme.typography.labelMedium,
                                                color = MaterialTheme.colorScheme.tertiary
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = String.format("~$%.2f", ing.estimatedCostUsd * servings),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (index < recipe.ingredients.lastIndex) {
                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = onAddMissingToGrocery,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .minimumInteractiveComponentSize()
                        .testTag("add_missing_grocery_button")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AddShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Missing Items to Grocery List")
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Quick Steps",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        // Cooking Steps with Per-Step Timers
        items(recipe.steps, key = { it.stepNumber }) { step ->
            Card(
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("cooking_step_${step.stepNumber}")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "${step.stepNumber}",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = step.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        if (step.durationSeconds != null) {
                            val mins = step.durationSeconds / 60
                            val secs = step.durationSeconds % 60
                            val formatted = String.format("%d:%02d", mins, secs)
                            FilledTonalButton(
                                onClick = {
                                    onStartTimer("Step ${step.stepNumber}: ${step.title}", step.durationSeconds)
                                },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .testTag("step_timer_button_${step.stepNumber}")
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Timer,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = formatted,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = step.instruction,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Dorm Hack Callout
        item {
            Surface(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Lightbulb,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Dorm Kitchen Hack",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = recipe.dormHack,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatPill(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(vertical = 10.dp, horizontal = 6.dp)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(
                fontFamily = MaterialTheme.typography.labelSmall.fontFamily
            ),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
