package com.infomerica.insightify.ui.composables.login.google_sign_in

import androidx.annotation.Keep

@Keep
data class LoginResult(
    val data: UserProfile?,
    val error: String?
)

@Keep
data class UserProfile(
    val user_id: String? = null,
    val username: String? = null,
    val profile_url: String? = null,
    val email: String? = null,
    val registered_on: String? = null,
    val conversation_ID: String? = null,
)

@Keep
data class UserConfiguration(
    val user_id: String? = null,
    val premium_user: Boolean? = null,
    val free_sessions: Int? = null
)

@Keep
data class UserMetaData(
    val user_profile_hash: String? = null,
    val user_configuration_hash: String? = null,
)


@Keep
data class UserConfigurationDto(
    val userId: String? = null,
    val premiumUser: Boolean? = null,
    val freeSessions: Int? = null
)

@Keep
data class UserProfileDto(
    val userId: String? = null,
    val username: String? = null,
    val profileUrl: String? = null,
    val email: String? = null,
    val registeredOn: String? = null,
    val conversationID: String? = null
)

@Keep
data class UserMetaDataDto(
    val userProfileHash: String? = null,
    val userConfigurationHash: String? = null,
)

fun UserProfile.toUserProfileDto() : UserProfileDto {
    return UserProfileDto(
        userId = user_id,
        username = username,
        profileUrl = profile_url,
        email = email,
        registeredOn = registered_on,
        conversationID = conversation_ID
    )
}

fun UserConfiguration.toUserConfigurationDto() : UserConfigurationDto {
    return UserConfigurationDto(
        userId = user_id,
        premiumUser = premium_user,
        freeSessions = free_sessions
    )
}

fun UserMetaData.toUserMetaDataDto() : UserMetaDataDto {
    return UserMetaDataDto(
        user_profile_hash,
        user_configuration_hash,
    )
}