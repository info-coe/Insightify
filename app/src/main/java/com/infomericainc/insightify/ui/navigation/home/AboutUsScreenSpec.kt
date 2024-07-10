package com.infomericainc.insightify.ui.navigation.home

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import com.infomericainc.insightify.ui.composables.aboutus.AboutUsScreen
import com.infomericainc.insightify.ui.composables.home.HomeViewModel
import com.infomericainc.insightify.ui.composables.shared.SharedViewModel

data object AboutUsScreenSpec : HomeSpec {
    override val route: String
        get() = HomeScreens.AboutUsScreenSpec.route

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
        windowWidthSizeClass: WindowWidthSizeClass,
    ) {
        AboutUsScreen(navController = navController)
    }


    override fun enterTransition(): EnterTransition {
        return slideInHorizontally()
    }

    override fun exitTransition(): ExitTransition {
        return slideOutHorizontally()
    }


}