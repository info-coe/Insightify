package com.infomericainc.insightify.ui.navigation.home

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import com.infomericainc.insightify.ui.composables.home.HomeViewModel
import com.infomericainc.insightify.ui.composables.recentorders.RecentOrderScreen
import com.infomericainc.insightify.ui.composables.recentorders.RecentOrdersEvent
import com.infomericainc.insightify.ui.composables.recentorders.RecentOrdersViewModel
import com.infomericainc.insightify.ui.composables.shared.SharedViewModel

data object RecentOrderScreenSpec : HomeSpec {
    override val route: String
        get() = HomeScreens.RecentOrderScreen.route

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
        val recentOrdersViewModel: RecentOrdersViewModel = hiltViewModel()
        val recentOrdersUiState by recentOrdersViewModel.recentOrderUiState.collectAsStateWithLifecycle()
        LaunchedEffect(key1 = Unit) {
            // TODO: Fix this
            recentOrdersViewModel
                .onEvent(RecentOrdersEvent.GetRecentOrders)
        }
        RecentOrderScreen(
            navController = navController,
            onOrderEvent = recentOrdersViewModel::onEvent,
            recentOrdersUiState
        )
    }


    override fun enterTransition(): EnterTransition {
        return slideInHorizontally()
    }

    override fun exitTransition(): ExitTransition {
        return slideOutHorizontally()
    }


}