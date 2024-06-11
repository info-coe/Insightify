package com.infomerica.insightify.ui.navigation.on_boarding

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink

sealed interface OnBoardingScreenSpec {

    companion object {
        val allScreens = listOf(
            SplashScreenSpec,
            WelcomeScreenSpec,
            LoginScreenSpec
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
        windowSizeClass: WindowSizeClass
    )

    fun enterTransition() : EnterTransition

    fun exitTransition() : ExitTransition

}