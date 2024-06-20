package com.infomerica.insightify.ui.navigation.downloads

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink

sealed interface DownloadsSpec {

    companion object {
        val allScreens = listOf(
            DownloadsScreenSpec
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
        navBackStackEntry: NavBackStackEntry
    )

    fun enterTransition() : EnterTransition

    fun exitTransition() : ExitTransition

}