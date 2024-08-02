package com.infomericainc.insightify.ui.composables.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.infomericainc.insightify.db.dao.UserConfigurationDao
import com.infomericainc.insightify.db.dao.UserProfileDao
import com.infomericainc.insightify.db.entites.UserProfileEntity
import com.infomericainc.insightify.db.entites.toUserConfigurationDto
import com.infomericainc.insightify.db.entites.toUserProfileDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val userProfileDao: UserProfileDao,
    private val userConfigurationDao: UserConfigurationDao,
    private val firebaseCrashlytics: FirebaseCrashlytics
) : ViewModel() {

    private val mutableUserConfigurationUiState: MutableStateFlow<UserConfigurationUiState> =
        MutableStateFlow(UserConfigurationUiState())
    val userConfigurationUiState: StateFlow<UserConfigurationUiState> =
        mutableUserConfigurationUiState.asStateFlow()


    private val mutableUserProfileUiState: MutableStateFlow<UserProfileUiState> =
        MutableStateFlow(UserProfileUiState())
    val userProfileUiState: StateFlow<UserProfileUiState> =
        mutableUserProfileUiState.asStateFlow()


    init {
        Timber.tag("HOME_VM").i("Initialized")
    }

    override fun onCleared() {
        super.onCleared()
        Timber.tag("HOME_VM").i("Cleared")
    }

    fun onTriggerEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.FetchUserConfigurationFromRoom -> fetchUserConfigurationFromRoom()
            is HomeEvent.FetchUserProfileFromRoom -> fetchUserProfileFromRoom()
        }
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is CancellationException -> {
                //HANDLE IT
            }

            else -> {
                firebaseCrashlytics
                    .recordException(throwable)
            }
        }
    }



    private suspend fun fetchUserProfile(): UserProfileEntity? {
        val rowCount = withContext(Dispatchers.IO) {
            userProfileDao
                .getRowCount()
        }

        //Table is empty
        if (rowCount == 0) {
            throw IllegalArgumentException()
        }

        return withContext(Dispatchers.IO) {
            userProfileDao
                .getUserProfile()
                .firstOrNull()
        }
    }

    private fun fetchUserConfigurationFromRoom() {

        mutableUserConfigurationUiState.update {
            it.copy(
                isLoading = true
            )
        }

        viewModelScope.launch(exceptionHandler) {
            val userProfileEntity = fetchUserProfile()

            val userConfigurationEntity = withContext(Dispatchers.IO) {
                userConfigurationDao
                    .getUserConfiguration()
                    .firstOrNull()
            }

            userProfileEntity.takeIf { it != null }?.let { userProfileEntity ->
                userConfigurationEntity.takeIf { it != null }?.let { userConfigurationEntity ->
                    userProfileEntity.email.takeIf { it != null }?.let { email ->
                        mutableUserConfigurationUiState.update {
                            it.copy(
                                isLoading = false,
                                userConfigurationDto = userConfigurationEntity.toUserConfigurationDto()
                            )
                        }
                    } ?: throw IllegalArgumentException()
                } ?: throw IllegalArgumentException()
            } ?: throw IllegalArgumentException()
        }
    }

    private fun fetchUserProfileFromRoom() {
        mutableUserProfileUiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            val userProfile = fetchUserProfile()
            userProfile.takeIf { it != null }?.let { userProfileEntity ->
                mutableUserProfileUiState.update {
                    it.copy(
                        isLoading = false,
                        userProfileDto = userProfileEntity.toUserProfileDto()
                    )
                }
            } ?: run {
                mutableUserProfileUiState.update {
                    it.copy(
                        isLoading = false,
                    )
                }
            }
        }
    }
}
