package com.infomerica.insightify.ui.composables.profile

import com.infomerica.insightify.ui.composables.login.google_sign_in.UserProfileDto


sealed class ProfileEvent {
    data object FetchUserProfile : ProfileEvent()
}


data class UserProfileUiState(
    val isLoading : Boolean = false,
    val userProfile: UserProfileDto? = null,
    val error : String? = null
)