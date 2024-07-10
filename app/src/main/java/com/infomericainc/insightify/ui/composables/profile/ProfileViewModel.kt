package com.infomericainc.insightify.ui.composables.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infomericainc.insightify.db.dao.UserProfileDao
import com.infomericainc.insightify.db.entites.toUserProfileDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userProfileDao: UserProfileDao
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Timber
            .tag("EXCEPTION")
            .d(throwable.message.toString())
    }

    private val mutableRecentOrdersUiState =
        MutableStateFlow(UserProfileUiState())
    val recentOrderUiState = mutableRecentOrdersUiState.asStateFlow()

    fun onTriggerEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.FetchUserProfile -> fetchUserProfileFromRoom()
        }
    }

    init {
        fetchUserProfileFromRoom()
    }

    private fun fetchUserProfileFromRoom() {
        viewModelScope.launch(exceptionHandler) {
            userProfileDao
                .getUserProfile()
                .onEach { userProfileEntity ->
                    mutableRecentOrdersUiState
                        .update {
                            it.copy(
                                isLoading = false,
                                userProfile = userProfileEntity.toUserProfileDto()
                            )
                        }
                }.launchIn(this)
        }
    }


}