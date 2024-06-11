package com.infomerica.insightify.ui.composables.home

import com.infomerica.insightify.ui.composables.home.recentsessions.RecentSessionModel
import com.infomerica.insightify.ui.composables.login.google_sign_in.UserConfigurationDto
import com.infomerica.insightify.ui.composables.login.google_sign_in.UserProfile
import com.infomerica.insightify.ui.composables.login.google_sign_in.UserProfileDto

sealed class HomeEvent {
    data object FetchUserConfigurationFromRoom : HomeEvent()
    data object FetchUserProfileFromRoom : HomeEvent()

    data object FetchRecentSessionDataFromRoom : HomeEvent()
    data class UpdateFavouriteToFirebase(val sessionId : String) : HomeEvent()
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
data class RecentSessionUiState(
    val isLoading : Boolean = false,
    val recentSessions: List<RecentSessionModel>? = null,
    val error : String? = ""
)

data class RecentSessionFavouriteUiState(
    val isUpdating : Boolean = false,
    val updatedSuccessfully: Boolean = false,
    val error : String? = ""
)