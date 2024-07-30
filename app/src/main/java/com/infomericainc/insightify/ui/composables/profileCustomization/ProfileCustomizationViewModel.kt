package com.infomericainc.insightify.ui.composables.profileCustomization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infomericainc.insightify.db.dao.UserProfileDao
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
    val userProfileDao: UserProfileDao,
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
            userProfileDao
                .getUserProfile()
                .firstOrNull()?.let { userProfileDto ->
                    if (userProfileDto.username != null) {
                        mutableUserProfileUIState
                            .update {
                                it.copy(
                                    isLoading = false,
                                    userProfileDto = userProfileDto.toUserProfileDto()
                                )
                            }
                    }
                } ?: run {
                mutableUserProfileUIState
                    .update {
                        it.copy(
                            isLoading = false,
                            error = "User profile not found"
                        )
                    }
            }
        }
    }
}