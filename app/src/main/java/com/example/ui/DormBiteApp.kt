package com.example.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Kitchen
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.R
import com.example.data.UserPreferencesRepository
import com.example.ui.components.ActiveTimerBanner
import com.example.ui.screens.DiscoverScreen
import com.example.ui.screens.FridgeMatchScreen
import com.example.ui.screens.RecipeDetailScreen
import com.example.ui.screens.SavedScreen

object DormBiteRoutes {
    const val ROUTE_DISCOVER = "discover"
    const val ROUTE_PANTRY = "pantry"
    const val ROUTE_SAVED = "saved"
    const val ROUTE_DETAIL = "recipe_detail/{recipeId}"

    fun detailRoute(recipeId: String): String = "recipe_detail/$recipeId"
}

private enum class TopLevelTab(
    val route: String,
    val labelRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    DISCOVER(
        route = DormBiteRoutes.ROUTE_DISCOVER,
        labelRes = R.string.nav_discover,
        selectedIcon = Icons.Filled.Explore,
        unselectedIcon = Icons.Outlined.Explore,
        testTag = "nav_discover"
    ),
    PANTRY(
        route = DormBiteRoutes.ROUTE_PANTRY,
        labelRes = R.string.nav_pantry,
        selectedIcon = Icons.Filled.Kitchen,
        unselectedIcon = Icons.Outlined.Kitchen,
        testTag = "nav_pantry"
    ),
    SAVED(
        route = DormBiteRoutes.ROUTE_SAVED,
        labelRes = R.string.nav_saved,
        selectedIcon = Icons.Filled.Bookmark,
        unselectedIcon = Icons.Outlined.BookmarkBorder,
        testTag = "nav_saved"
    )
}

@Composable
fun DormBiteApp() {
    val context = LocalContext.current.applicationContext
    val repository = remember(context) { UserPreferencesRepository(context) }
    val viewModel: DormBiteViewModel = viewModel(factory = DormBiteViewModel.Factory(repository))
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val isDetailRoute = currentDestination?.route?.startsWith("recipe_detail") == true

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(uiState.snackbarMessage) {
        val message = uiState.snackbarMessage
        if (message != null) {
            snackbarHostState.showSnackbar(message)
            viewModel.consumeSnackbar()
        }
    }

    val navigateTopLevel: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val useNavigationRail = maxWidth >= 600.dp

        Scaffold(
            contentWindowInsets = if (isDetailRoute) WindowInsets(0) else WindowInsets.safeDrawing,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            bottomBar = {
                if (!useNavigationRail && !isDetailRoute) {
                    Column {
                        ActiveTimerBanner(
                            timerState = uiState.timerState,
                            onOpenRecipe = { recipeId ->
                                navController.navigate(DormBiteRoutes.detailRoute(recipeId))
                            },
                            onTogglePause = viewModel::toggleTimerRunning,
                            onDismiss = viewModel::dismissTimer
                        )
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                        ) {
                            TopLevelTab.entries.forEach { tab ->
                                val selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = { navigateTopLevel(tab.route) },
                                    icon = {
                                        Icon(
                                            imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                            contentDescription = stringResource(tab.labelRes)
                                        )
                                    },
                                    label = { Text(stringResource(tab.labelRes)) },
                                    modifier = Modifier.testTag(tab.testTag)
                                )
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (useNavigationRail && !isDetailRoute) {
                    NavigationRail(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        TopLevelTab.entries.forEach { tab ->
                            val selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true
                            NavigationRailItem(
                                selected = selected,
                                onClick = { navigateTopLevel(tab.route) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                        contentDescription = stringResource(tab.labelRes)
                                    )
                                },
                                label = { Text(stringResource(tab.labelRes)) },
                                modifier = Modifier.testTag(tab.testTag)
                            )
                        }
                    }
                }

                Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    if (useNavigationRail && !isDetailRoute) {
                        ActiveTimerBanner(
                            timerState = uiState.timerState,
                            onOpenRecipe = { recipeId ->
                                navController.navigate(DormBiteRoutes.detailRoute(recipeId))
                            },
                            onTogglePause = viewModel::toggleTimerRunning,
                            onDismiss = viewModel::dismissTimer
                        )
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        NavHost(
                            navController = navController,
                            startDestination = DormBiteRoutes.ROUTE_DISCOVER,
                            enterTransition = { EnterTransition.None },
                            exitTransition = { ExitTransition.None }
                        ) {
                            composable(DormBiteRoutes.ROUTE_DISCOVER) {
                                DiscoverScreen(
                                    uiState = uiState,
                                    onSearchChange = viewModel::updateSearchQuery,
                                    onSelectEquipment = viewModel::selectEquipment,
                                    onSelectMealFilter = viewModel::selectMealFilter,
                                    onClearFilters = viewModel::clearFilters,
                                    onRecipeClick = { id ->
                                        navController.navigate(DormBiteRoutes.detailRoute(id))
                                    },
                                    onToggleFavorite = viewModel::toggleFavorite
                                )
                            }

                            composable(DormBiteRoutes.ROUTE_PANTRY) {
                                FridgeMatchScreen(
                                    uiState = uiState,
                                    onToggleStaple = viewModel::togglePantryStaple,
                                    onApplyStarterKit = viewModel::applyStarterDormKit,
                                    onClearPantry = viewModel::clearPantry,
                                    onRecipeClick = { id ->
                                        navController.navigate(DormBiteRoutes.detailRoute(id))
                                    },
                                    onToggleFavorite = viewModel::toggleFavorite
                                )
                            }

                            composable(DormBiteRoutes.ROUTE_SAVED) {
                                SavedScreen(
                                    uiState = uiState,
                                    onRecipeClick = { id ->
                                        navController.navigate(DormBiteRoutes.detailRoute(id))
                                    },
                                    onToggleFavorite = viewModel::toggleFavorite,
                                    onToggleGroceryChecked = viewModel::toggleGroceryChecked,
                                    onRemoveGroceryItem = viewModel::removeGroceryItem,
                                    onClearCheckedGrocery = viewModel::clearCheckedGroceryItems,
                                    onAddCustomGroceryItem = { name, amount, cost ->
                                        viewModel.addSingleIngredientToGroceryList(
                                            name = name,
                                            amountDisplay = amount.ifBlank { "1 item" },
                                            cost = cost,
                                            key = "custom_${name.lowercase().replace(" ", "_")}_${System.currentTimeMillis()}"
                                        )
                                    }
                                )
                            }

                            composable(
                                route = DormBiteRoutes.ROUTE_DETAIL,
                                arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
                            ) { backStackEntry ->
                                val recipeId = backStackEntry.arguments?.getString("recipeId").orEmpty()
                                val recipe = viewModel.getRecipeById(recipeId) ?: uiState.featuredRecipe
                                RecipeDetailScreen(
                                    recipe = recipe,
                                    uiState = uiState,
                                    onBackClick = { navController.popBackStack() },
                                    onToggleFavorite = { viewModel.toggleFavorite(recipe.id) },
                                    onServingsChange = viewModel::setServings,
                                    onAddMissingToGrocery = { viewModel.addMissingToGroceryList(recipe) },
                                    onStartTimer = { label, seconds ->
                                        viewModel.startTimer(recipe, label, seconds)
                                    },
                                    onToggleTimerPause = viewModel::toggleTimerRunning,
                                    onResetTimer = viewModel::resetTimer
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
