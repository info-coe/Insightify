package com.infomerica.insightify.ui.navigation.profile

sealed class ProfileScreens(val route : String) {
    data object ProfileScreen : ProfileScreens("profile_screen")
}
