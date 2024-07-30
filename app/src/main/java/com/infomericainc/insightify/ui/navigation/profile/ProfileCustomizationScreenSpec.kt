package com.infomericainc.insightify.ui.navigation.profile

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
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
import com.infomericainc.insightify.ui.composables.profileCustomization.ProfileCustomizationEvent
import com.infomericainc.insightify.ui.composables.profileCustomization.ProfileCustomizationScreen
import com.infomericainc.insightify.ui.composables.profileCustomization.ProfileCustomizationViewModel

data object ProfileCustomizationScreenSpec : ProfileSpec {
    override val route: String
        get() = ProfileScreens.ProfileCustomizationScreen.route

    override val arguments: List<NamedNavArgument>
        get() = super.arguments

    override val deepLinks: List<NavDeepLink>
        get() = super.deepLinks


    @Composable
    override fun Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry,
        windowWidthSizeClass: WindowWidthSizeClass,
    ) {
        val profileCustomizationViewModel: ProfileCustomizationViewModel = hiltViewModel()
        LaunchedEffect(Unit) {
            profileCustomizationViewModel
                .onEvent(
                    event = ProfileCustomizationEvent.FetchUserData
                )
        }
        val userProfileUIState by profileCustomizationViewModel.userProfileUIState.collectAsStateWithLifecycle()
        ProfileCustomizationScreen(
            windowWidthSizeClass = windowWidthSizeClass,
            navController = navController,
            userProfileUIState = userProfileUIState
        )
    }

    override fun enterTransition(): EnterTransition {
        return slideInHorizontally(animationSpec = tween())
    }

    override fun exitTransition(): ExitTransition {
        return slideOutHorizontally(animationSpec = tween())
    }

}