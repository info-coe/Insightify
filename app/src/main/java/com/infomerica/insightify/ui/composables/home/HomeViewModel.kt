package com.infomerica.insightify.ui.composables.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.infomerica.insightify.db.dao.RecentSessionDao
import com.infomerica.insightify.db.dao.UserConfigurationDao
import com.infomerica.insightify.db.dao.UserMetaDataDao
import com.infomerica.insightify.db.dao.UserProfileDao
import com.infomerica.insightify.db.entites.RecentSessionEntity
import com.infomerica.insightify.db.entites.UserProfileEntity
import com.infomerica.insightify.db.entites.toUserConfigurationDto
import com.infomerica.insightify.db.entites.toUserProfileDto
import com.infomerica.insightify.ui.composables.home.recentsessions.RecentSessionModel
import com.infomerica.insightify.ui.composables.login.google_sign_in.UserMetaData
import com.infomerica.insightify.ui.composables.login.google_sign_in.UserMetaDataDto
import com.infomerica.insightify.ui.composables.login.google_sign_in.toUserMetaDataDto
import com.infomerica.insightify.util.Constants
import com.infomerica.insightify.util.Constants.LAST_SEEN
import com.infomerica.insightify.util.Constants.SERVER_TIME_STAMP
import com.infomerica.insightify.util.Constants.SESSION_ID
import com.infomerica.insightify.util.Constants.STARRED
import com.infomerica.insightify.util.Constants.TITLE
import com.infomerica.insightify.util.Constants.UNABLE_TO_UPDATE_FAVOURITE
import com.infomerica.insightify.util.Constants.cancellationExceptionErrorMessage
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
    private val recentSessionDao: RecentSessionDao,
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

    private val mutableRecentSessionUiState: MutableStateFlow<RecentSessionUiState> =
        MutableStateFlow(RecentSessionUiState())
    val recentSessionUiState: StateFlow<RecentSessionUiState> =
        mutableRecentSessionUiState.asStateFlow()

    private val mutableRecentSessionFavouriteUiState: MutableStateFlow<RecentSessionFavouriteUiState> =
        MutableStateFlow(RecentSessionFavouriteUiState())
    val recentSessionFavouriteUiState: StateFlow<RecentSessionFavouriteUiState> =
        mutableRecentSessionFavouriteUiState.asStateFlow()


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
            is HomeEvent.FetchRecentSessionDataFromRoom -> saveRecentSessionToRoom()
            is HomeEvent.UpdateFavouriteToFirebase -> updateFavouriteToFirebase(event.sessionId)
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

    /**
     * Used to update the Fav/Starred for RecentSessions
     * @param sessionId the session Id for conversation.
     */
    private fun updateFavouriteToFirebase(sessionId: String) {
        mutableRecentSessionFavouriteUiState.update {
            it.copy(isUpdating = true)
        }
        viewModelScope.launch(exceptionHandler) {
            try {
                val userProfileEntity = fetchUserProfile()
                userProfileEntity.takeIf { it != null }?.let { userProfile ->
                    userProfile.email?.takeIf { it.isNotEmpty() }?.let { email ->
                        fireStore
                            .collection(Constants.RecentSessionPath.USERS.name)
                            .document(email)
                            .collection(Constants.RecentSessionPath.CONVERSATIONS.name)
                            .document(sessionId)
                            .set(mapOf(STARRED to true), SetOptions.merge())
                            .await()
                        mutableRecentSessionFavouriteUiState.update {
                            it.copy(isUpdating = false, updatedSuccessfully = true)
                        }
                    } ?: run {
                        mutableRecentSessionFavouriteUiState.update {
                            it.copy(isUpdating = false, error = UNABLE_TO_UPDATE_FAVOURITE)
                        }
                    }
                } ?: run {
                    mutableRecentSessionFavouriteUiState.update {
                        it.copy(isUpdating = false, error = UNABLE_TO_UPDATE_FAVOURITE)
                    }
                }
            } catch (e: CancellationException) {
                mutableRecentSessionFavouriteUiState.update {
                    it.copy(isUpdating = false, error = UNABLE_TO_UPDATE_FAVOURITE)
                }
                Timber.tag(HOME_VM_EXCEPTION)
                    .i(e, message = cancellationExceptionErrorMessage("UpdateFavToFirebase"))
                throw e
            }
        }
    }

    private suspend fun getRecentSessionDataFromFirebase(): List<RecentSessionModel> =
        withContext(Dispatchers.IO) {
            val userProfileEntity = fetchUserProfile()
            val recentSessionsList: MutableList<RecentSessionModel> = mutableListOf()

            userProfileEntity?.email?.takeIf { it.isNotEmpty() }?.let { email ->
                val querySnapshot = fireStore
                    .collection(Constants.RecentSessionPath.USERS.name)
                    .document(email)
                    .collection(Constants.RecentSessionPath.CONVERSATIONS.name)
                    .orderBy(SERVER_TIME_STAMP, Query.Direction.DESCENDING)
                    .get()
                    .await()

                querySnapshot?.takeIf { it.documents.isNotEmpty() }?.let { snapshot ->
                    recentSessionsList.clear()
                    for (documentSnapshot in snapshot.documents) {
                        documentSnapshot.get(TITLE, String::class.java)?.let { title ->
                            documentSnapshot.get(STARRED, Boolean::class.java)?.let { starred ->
                                documentSnapshot.get(LAST_SEEN, String::class.java)
                                    ?.let { lastSeen ->
                                        documentSnapshot.get(SESSION_ID, String::class.java)
                                            ?.let { sessionId ->
                                                recentSessionsList.add(
                                                    RecentSessionModel(
                                                        title,
                                                        lastSeen,
                                                        starred,
                                                        sessionId
                                                    )
                                                )
                                            }
                                    }
                            }
                        }
                    }
                }
            }
            recentSessionsList
        }

    private fun saveRecentSessionToRoom() {
        viewModelScope.launch(exceptionHandler) {
            mutableRecentSessionUiState.update {
                it.copy(
                    isLoading = true
                )
            }
            try {
                getRecentSessionDataFromFirebase().let { recentSessionModels ->
                    withContext(Dispatchers.IO) {
                        recentSessionModels.forEach { recentSessionModel ->
                            recentSessionDao.saveRecentSessionData(
                                recentSessionEntity = RecentSessionEntity(
                                    id = recentSessionModel.sessionId,
                                    title = recentSessionModel.title,
                                    lastSeen = recentSessionModel.lastSeen,
                                    starred = recentSessionModel.starred,
                                    conversation = recentSessionModel.conversation
                                )
                            )
                        }
                    }
                    mutableRecentSessionUiState.update {
                        it.copy(
                            isLoading = false,
                            recentSessions = recentSessionModels
                        )
                    }
                }
            } catch (e: Exception) {
                if (e is CancellationException) {
                    mutableRecentSessionUiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Unable to get Recent session."
                        )
                    }
                    Timber.tag(HOME_VM_EXCEPTION)
                        .i(e, message = cancellationExceptionErrorMessage("UpdateFavToFirebase"))
                    throw e
                } else {
                    Timber.tag(HOME_VM_EXCEPTION).i(e)
                    mutableRecentSessionUiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Unable to get Recent session."
                        )
                    }
                }
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