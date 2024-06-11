package com.infomerica.insightify.ui.navigation.home

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.infomerica.insightify.ui.composables.home.HomeViewModel
import com.infomerica.insightify.ui.composables.recentOrderRating.RecentOrderRatingViewModel
import com.infomerica.insightify.ui.composables.recentOrderRating.RecentOrderReviewScreen
import com.infomerica.insightify.ui.composables.shared.SharedViewModel

data object RecentOrderReviewScreenSpec : HomeSpec {
    override val route: String
        get() = HomeScreens.RecentOrderReviewScreen.route

    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument("orders") {
                type = NavType.StringArrayType
            }
        )

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
        val recentOrderRatingViewModel : RecentOrderRatingViewModel = hiltViewModel()
        val recentOrders by recentOrderRatingViewModel.recentOrderUiState.collectAsStateWithLifecycle()
        RecentOrderReviewScreen(
            navController = navController,
            recentOrderUiState = recentOrders,
            onEvent = recentOrderRatingViewModel::onTriggerEvent
        )
    }


    override fun enterTransition(): EnterTransition {
        return slideInHorizontally()
    }

    override fun exitTransition(): ExitTransition {
        return slideOutHorizontally()
    }


}