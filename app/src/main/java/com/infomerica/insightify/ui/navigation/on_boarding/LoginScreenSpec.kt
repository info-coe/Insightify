package com.infomerica.insightify.ui.navigation.on_boarding

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import com.infomerica.insightify.ui.composables.login.LoginScreen
import com.infomerica.insightify.ui.composables.login.LoginViewModel

data object LoginScreenSpec : OnBoardingScreenSpec {
    override val route: String
        get() = OnBoardingScreens.LoginScreen.route

    override val arguments: List<NamedNavArgument>
        get() = super.arguments

    override val deepLinks: List<NavDeepLink>
        get() = super.deepLinks


    @Composable
    override fun Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry,
        windowSizeClass: WindowSizeClass
    ) {
        val loginViewModel: LoginViewModel = hiltViewModel()
        val userRegistrationUiState = loginViewModel.userRegistrationUiState.collectAsStateWithLifecycle()
        LoginScreen(navController,windowSizeClass.widthSizeClass,userRegistrationUiState.value, event = loginViewModel::onTriggerEvent)
    }

    override fun enterTransition(): EnterTransition {
        return slideInHorizontally(animationSpec = tween())
    }

    override fun exitTransition(): ExitTransition {
        return slideOutHorizontally(animationSpec = tween())
    }


}