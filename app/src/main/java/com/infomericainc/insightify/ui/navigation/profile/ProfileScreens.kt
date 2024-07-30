package com.infomericainc.insightify.ui.navigation.profile

sealed class ProfileScreens(val route: String) {
    data object ProfileScreen : ProfileScreens("profile_screen")
    data object ProfileCustomizationScreen : ProfileScreens("profile_customization_screen")
    data object AboutScreenSpec : ProfileScreens("about_screen")
    data object SettingsScreen : ProfileScreens("settings_screen")
    data object ThreadsScreen : ProfileScreens("threads_screen")
}
