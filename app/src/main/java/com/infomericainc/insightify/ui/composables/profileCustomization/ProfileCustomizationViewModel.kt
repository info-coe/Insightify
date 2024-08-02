package com.infomericainc.insightify.ui.composables.profileCustomization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infomericainc.insightify.db.dao.UserConfigurationDao
import com.infomericainc.insightify.db.dao.UserProfileDao
import com.infomericainc.insightify.db.entites.toUserConfigurationDto
import com.infomericainc.insightify.db.entites.toUserProfileDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileCustomizationViewModel @Inject constructor(
    private val userProfileDao: UserProfileDao,
    private val userConfigurationDao: UserConfigurationDao
) : ViewModel() {
    private val mutableUserProfileUIState = MutableStateFlow(ProfileUIState())
    val userProfileUIState = mutableUserProfileUIState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->

    }

    fun onEvent(event: ProfileCustomizationEvent) {
        when (event) {
            is ProfileCustomizationEvent.FetchUserData -> {
                fetchUserData()
            }
        }
    }

    private fun fetchUserData() {
        mutableUserProfileUIState
            .update {
                it.copy(
                    isLoading = true
                )
            }
        viewModelScope.launch(exceptionHandler) {
            val userProfile = userProfileDao
                .getUserProfile()
                .firstOrNull() ?: run {
                mutableUserProfileUIState
                    .update {
                        it.copy(
                            isLoading = false,
                            error = "User profile not found"
                        )
                    }
                return@launch
            }

            val userConfiguration = userConfigurationDao
                .getUserConfiguration()
                .firstOrNull() ?: kotlin.run {
                mutableUserProfileUIState
                    .update {
                        it.copy(
                            isLoading = false,
                            error = "User profile not found"
                        )
                    }
                return@launch
            }

            mutableUserProfileUIState
                .update {
                    it.copy(
                        isLoading = false,
                        userProfileDto = userProfile.toUserProfileDto(),
                        userConfigurationDto = userConfiguration.toUserConfigurationDto().also(
                            ::println
                        )
                    )
                }
        }
    }
}