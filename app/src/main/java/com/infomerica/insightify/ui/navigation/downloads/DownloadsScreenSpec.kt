package com.infomerica.insightify.ui.navigation.downloads

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import com.infomerica.insightify.ui.composables.downloads.DownloadsScreen
import com.infomerica.insightify.ui.navigation.profile.ProfileScreens

data object DownloadsScreenSpec : DownloadsSpec {
    override val route: String
        get() = DownloadScreens.DownloadsScreen.route

    override val arguments: List<NamedNavArgument>
        get() = super.arguments

    override val deepLinks: List<NavDeepLink>
        get() = super.deepLinks


    @Composable
    override fun Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry
    ) {
        DownloadsScreen(navController = navController)
    }

    override fun enterTransition(): EnterTransition {
        return slideInHorizontally(animationSpec = tween())
    }

    override fun exitTransition(): ExitTransition {
        return slideOutHorizontally(animationSpec = tween())
    }

}