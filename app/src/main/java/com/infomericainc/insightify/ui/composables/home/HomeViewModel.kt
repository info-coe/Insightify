package com.infomericainc.insightify.ui.composables.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.infomericainc.insightify.db.dao.UserConfigurationDao
import com.infomericainc.insightify.db.dao.UserMetaDataDao
import com.infomericainc.insightify.db.dao.UserProfileDao
import com.infomericainc.insightify.db.entites.UserProfileEntity
import com.infomericainc.insightify.db.entites.toUserConfigurationDto
import com.infomericainc.insightify.db.entites.toUserProfileDto
import com.infomericainc.insightify.ui.composables.login.google_sign_in.UserMetaData
import com.infomericainc.insightify.ui.composables.login.google_sign_in.UserMetaDataDto
import com.infomericainc.insightify.ui.composables.login.google_sign_in.toUserMetaDataDto
import com.infomericainc.insightify.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val userProfileDao: UserProfileDao,
    private val userConfigurationDao: UserConfigurationDao,
    private val userMetaDataDao: UserMetaDataDao,
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
        fetchPendingOrders()
    }

    override fun onCleared() {
        super.onCleared()
        Timber.tag("HOME_VM").i(
            viewModelScope.coroutineContext.job.children.toList().size.toString()
                .plus(" - Active jobs")
        )
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
                //HANDLE IT
            }
        }
    }


    private suspend fun getProfileMetaDataFromFirebase(
        email: String,
        userId: String
    ): UserMetaDataDto? {
        return withContext(Dispatchers.IO + exceptionHandler) {
            val userMetaData = fireStore.collection(Constants.UserMetaDataPath.USERS.name)
                .document(email)
                .collection(Constants.UserMetaDataPath.META_DATA.name)
                .document(userId)
                .get()
                .await()
                .toObject(UserMetaData::class.java)
            return@withContext userMetaData?.toUserMetaDataDto()
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

            val userMetaDataEntity = withContext(Dispatchers.IO) {
                userMetaDataDao
                    .getUserMetaData()
                    .firstOrNull()
            }

            userProfileEntity.takeIf { it != null }?.let { userProfileEntity ->
                userConfigurationEntity.takeIf { it != null }?.let { userConfigurationEntity ->
                    userProfileEntity.email.takeIf { it != null }?.let { email ->
                        Timber.tag("HOME_VM_ROOM").i(userProfileEntity.toString())
                        Timber.tag("HOME_VM_ROOM").i(userConfigurationEntity.toString())
                        mutableUserConfigurationUiState.update {
                            it.copy(
                                isLoading = false,
                                userConfigurationDto = userConfigurationEntity.toUserConfigurationDto()
                            )
                        }
                        userMetaDataEntity.takeIf { it != null }?.let { userMetaDataEntity ->
                            getProfileMetaDataFromFirebase(
                                email,
                                userProfileEntity.userID
                            )?.let { userMetaDataDto ->
                                if (userMetaDataEntity.userProfileHash == userMetaDataDto.userProfileHash) {
                                    Timber.tag("HOME_VM_ROOM").i("user meta Matched")
                                } else {
                                    Timber.tag("HOME_VM_ROOM").i("user meta Changed")
                                }
                            }
                        } ?: throw IllegalArgumentException()
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

    private fun fetchPendingOrders() {
        viewModelScope.launch {
            fireStore
                .collection("USERS")
                .whereEqualTo("tableNumber",7)
                .whereEqualTo("orderStatus","PENDING")
                .get()
                .await()
                .documents
                .size
                .also(::println)
        }
    }
}

internal const val HOME_VM_EXCEPTION = "Home_vm_exception"