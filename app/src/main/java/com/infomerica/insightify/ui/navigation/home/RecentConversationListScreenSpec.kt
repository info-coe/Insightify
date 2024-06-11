package com.infomerica.insightify.ui.navigation.home

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import com.infomerica.insightify.ui.composables.home.HomeViewModel
import com.infomerica.insightify.ui.composables.home.recentsessions.RecentSessionsScreen
import com.infomerica.insightify.ui.composables.shared.SharedViewModel

data object RecentConversationListScreenSpec : HomeSpec {
    override val route: String
        get() = HomeScreens.RecentConversationListScreen.route

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
        val recentSessionUiState by homeViewModel.recentSessionUiState.collectAsStateWithLifecycle()
        RecentSessionsScreen(
            navController = navController,
            recentSessionUiState,
            onSharedEvent = {
                sharedViewModel.onTriggerEvent(it)
            }
        ) {
            homeViewModel.onTriggerEvent(it)
        }
    }



    override fun enterTransition(): EnterTransition {
        return slideInHorizontally()
    }

    override fun exitTransition(): ExitTransition {
        return slideOutHorizontally()
    }


}