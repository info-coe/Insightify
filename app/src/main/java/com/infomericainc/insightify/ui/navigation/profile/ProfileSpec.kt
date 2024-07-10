package com.infomericainc.insightify.ui.navigation.profile

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink

sealed interface ProfileSpec {

    companion object {
        val allScreens = listOf(
            ProfileScreenSpec,
            SettingsScreenSpec,
            ThreadsScreenSpec,
            PolicyScreenSpec
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
        windowWidthSizeClass: WindowWidthSizeClass
    )

    fun enterTransition() : EnterTransition

    fun exitTransition() : ExitTransition

}