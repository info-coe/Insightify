package com.infomerica.insightify.ui.navigation.home

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import com.infomerica.insightify.ui.composables.home.HomeViewModel
import com.infomerica.insightify.ui.composables.shared.SharedViewModel

sealed interface HomeSpec {

    companion object {
        val allScreens = listOf(
            HomeScreenSpec,
            GenericAssistantScreenSpec,
            RecentOrderScreenSpec,
            RecentOrderReviewScreenSpec,
            AboutUsScreenSpec
        )
    }

    val route : String

    val arguments : List<NamedNavArgument>
        get() = emptyList()

    val deepLinks : List<NavDeepLink>
        get() = emptyList()

    @Composable
    fun Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry,
        homeViewModel: HomeViewModel,
        sharedViewModel: SharedViewModel,
        windowWidthSizeClass: WindowWidthSizeClass
    )

    fun enterTransition() : EnterTransition

    fun exitTransition() : ExitTransition

}