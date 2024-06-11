package com.infomerica.insightify.ui.navigation.profile

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import com.infomerica.insightify.ui.composables.downloads.DownloadsScreen
import com.infomerica.insightify.ui.composables.profile.ProfileScreen
import com.infomerica.insightify.ui.composables.profile.ProfileViewModel
import com.infomerica.insightify.ui.navigation.profile.ProfileScreens
import com.infomerica.insightify.ui.navigation.profile.ProfileSpec

data object ProfileScreenSpec : ProfileSpec {
    override val route: String
        get() = ProfileScreens.ProfileScreen.route

    override val arguments: List<NamedNavArgument>
        get() = super.arguments

    override val deepLinks: List<NavDeepLink>
        get() = super.deepLinks


    @Composable
    override fun Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry
    ) {
        val profileViewModel: ProfileViewModel = hiltViewModel()
        val userProfileUiState by profileViewModel.recentOrderUiState.collectAsStateWithLifecycle()
        ProfileScreen(
            navController = navController,
            userProfileUiState = userProfileUiState
        )
    }

    override fun enterTransition(): EnterTransition {
        return slideInHorizontally(animationSpec = tween())
    }

    override fun exitTransition(): ExitTransition {
        return slideOutHorizontally(animationSpec = tween())
    }

}