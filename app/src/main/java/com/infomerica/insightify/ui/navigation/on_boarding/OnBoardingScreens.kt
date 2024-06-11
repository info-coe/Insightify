package com.infomerica.insightify.ui.navigation.on_boarding

sealed class OnBoardingScreens(val route : String) {
    object SplashScreen : OnBoardingScreens("splash_screen")
    object WelcomeScreen : OnBoardingScreens("welcome_screen")
    object LoginScreen : OnBoardingScreens("login_screen")

}
