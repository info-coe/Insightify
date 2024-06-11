package com.infomerica.insightify.ui.navigation.home

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import com.infomerica.insightify.ui.composables.home.HomeViewModel
import com.infomerica.insightify.ui.composables.home.RestaurantHomeScreen
import com.infomerica.insightify.ui.composables.shared.SharedViewModel

data object HomeScreenSpec : HomeSpec {
    override val route: String
        get() = HomeScreens.HomeScreen.route

    override val arguments: List<NamedNavArgument>
        get() = super.arguments

    override val deepLinks: List<NavDeepLink>
        get() = super.deepLinks


    @Composable
    override fun Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry,
        homeViewModel: HomeViewModel,
        sharedViewModel: SharedViewModel,
        windowWidthSizeClass: WindowWidthSizeClass
    ) {
        val userConfigurationUiState by homeViewModel.userConfigurationUiState.collectAsStateWithLifecycle()
        val recentSessionUiState by homeViewModel.recentSessionUiState.collectAsStateWithLifecycle()
        val favouriteUiState by homeViewModel.recentSessionFavouriteUiState.collectAsStateWithLifecycle()
        val userProfileUiState by homeViewModel.userProfileUiState.collectAsStateWithLifecycle()
//        HomeScreen(
//            navController = navController,
//            userConfigurationUiState = userConfigurationUiState,
//            recentSessionUiState = recentSessionUiState,
//            userProfileUiState = userProfileUiState,
//            onSharedEvent = sharedViewModel::onTriggerEvent,
//            updateFavouriteUiState = favouriteUiState,
//            onEvent = homeViewModel::onTriggerEvent
//        )
        RestaurantHomeScreen(
            navController = navController,
            userConfigurationUiState = userConfigurationUiState,
            userProfileUiState = userProfileUiState,
            onSharedEvent = sharedViewModel::onTriggerEvent,
            onEvent = homeViewModel::onTriggerEvent,
            windowWidthSizeClass = windowWidthSizeClass
        )
    }

    override fun enterTransition(): EnterTransition {
        return slideInHorizontally(animationSpec = tween())
    }

    override fun exitTransition(): ExitTransition {
        return slideOutHorizontally(animationSpec = tween())
    }

}