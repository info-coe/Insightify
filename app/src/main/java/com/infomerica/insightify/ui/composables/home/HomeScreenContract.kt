package com.infomerica.insightify.ui.composables.home

import com.infomerica.insightify.ui.composables.login.google_sign_in.UserConfigurationDto
import com.infomerica.insightify.ui.composables.login.google_sign_in.UserProfileDto

sealed class HomeEvent {
    data object FetchUserConfigurationFromRoom : HomeEvent()
    data object FetchUserProfileFromRoom : HomeEvent()
}

data class UserConfigurationUiState(
    val isLoading : Boolean = false,
    val userConfigurationDto: UserConfigurationDto? = null,
    val error : String? = ""
)

data class UserProfileUiState(
    val isLoading: Boolean = false,
    val userProfileDto: UserProfileDto? = null,
    val error: String? = ""
)
