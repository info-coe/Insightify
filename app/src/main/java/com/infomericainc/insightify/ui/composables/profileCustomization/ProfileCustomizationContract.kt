package com.infomericainc.insightify.ui.composables.profileCustomization

import com.infomericainc.insightify.ui.composables.login.google_sign_in.UserConfigurationDto
import com.infomericainc.insightify.ui.composables.login.google_sign_in.UserProfileDto

sealed class ProfileCustomizationEvent {
    data object FetchUserData : ProfileCustomizationEvent()
}

data class ProfileUIState(
    val isLoading: Boolean = false,
    val userProfileDto: UserProfileDto? = null,
    val userConfigurationDto: UserConfigurationDto? = null,
    val error: String? = null
)