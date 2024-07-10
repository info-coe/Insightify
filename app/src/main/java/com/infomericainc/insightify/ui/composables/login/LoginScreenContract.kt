package com.infomericainc.insightify.ui.composables.login

import com.infomericainc.insightify.ui.composables.login.google_sign_in.LoginResult
import com.infomericainc.insightify.ui.composables.login.google_sign_in.UserProfileDto

sealed class LoginEvents {
    data class SendUserDataToFireStore(
        val userData: LoginResult
    ) : LoginEvents()

}

data class UserRegistrationUiState(
    val isLoading : Boolean = false,
    val userDataDto: UserProfileDto = UserProfileDto(),
    val error : String = ""
)