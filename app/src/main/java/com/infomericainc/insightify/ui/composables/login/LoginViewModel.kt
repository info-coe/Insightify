package com.infomericainc.insightify.ui.composables.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.infomericainc.insightify.db.dao.UserConfigurationDao
import com.infomericainc.insightify.db.dao.UserProfileDao
import com.infomericainc.insightify.db.entites.UserConfigurationEntity
import com.infomericainc.insightify.db.entites.UserProfileEntity
import com.infomericainc.insightify.manager.PreferencesManager
import com.infomericainc.insightify.ui.composables.login.google_sign_in.LoginResult
import com.infomericainc.insightify.ui.composables.login.google_sign_in.UserConfiguration
import com.infomericainc.insightify.ui.composables.login.google_sign_in.UserConfigurationDto
import com.infomericainc.insightify.ui.composables.login.google_sign_in.UserProfile
import com.infomericainc.insightify.ui.composables.login.google_sign_in.UserProfileDto
import com.infomericainc.insightify.ui.composables.login.google_sign_in.toUserConfigurationDto
import com.infomericainc.insightify.ui.composables.login.google_sign_in.toUserProfileDto
import com.infomericainc.insightify.util.Constants
import com.infomericainc.insightify.util.Constants.TABLE_NUMBER
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val userProfileDao: UserProfileDao,
    private val userConfigurationDao: UserConfigurationDao,
    private val firebaseCrashlytics: FirebaseCrashlytics,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val mutableUserRegistrationUiState: MutableStateFlow<UserRegistrationUiState> =
        MutableStateFlow(UserRegistrationUiState())
    val userRegistrationUiState: StateFlow<UserRegistrationUiState> =
        mutableUserRegistrationUiState.asStateFlow()

    fun onTriggerEvent(event: LoginEvents) {
        when (event) {
            is LoginEvents.SendUserDataToFireStore -> verifyUserDataFromFireStore(event.userData)
        }
    }

    init {
        Timber.tag("LOGIN_VM").i("Initialized")
    }

    override fun onCleared() {
        super.onCleared()
        Timber.tag("LOGIN_VM").i(
            viewModelScope.coroutineContext.job.children.toList().size.toString()
                .plus(" - Active jobs")
        )
        Timber.tag("LOGIN_VM").i("Cleared")
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is IllegalArgumentException -> {
                firebaseCrashlytics
                    .recordException(throwable)
                mutableUserRegistrationUiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Unable to Login! Try again later."
                    )
                }
            }
        }
    }


    private fun verifyUserDataFromFireStore(loginResult: LoginResult) {
        viewModelScope.launch(exceptionHandler) {
            /**
             * Updating the state of our UI
             */
            mutableUserRegistrationUiState.update {
                it.copy(
                    isLoading = true,
                )
            }


            /**
             * Getting the userData from firebase to check if the user exists
             * or not.
             */

            val userDataResult = fireStore.collection(Constants.UserProfilePath.USERS.name)
                .document(
                    loginResult.data?.email ?: throw IllegalArgumentException("Email is null")
                )
                .collection(Constants.UserProfilePath.USER_DATA.name)
                .document(
                    loginResult.data.user_id ?: throw IllegalArgumentException("User ID is null")
                )
                .get()
                .await()
                .toObject(UserProfile::class.java)


            /**
             * If the email and the userId is not empty - we fetching the user configuration form firebase.
             * else we save the userProfile and UserConfiguration parallel and onSuccess we saving the
             * UserProfile,UserConfiguration to room and update our ui state.
             */

            userDataResult?.let { userData ->
                userData.email?.takeIf { it.isNotEmpty() }?.let { _ ->
                    userData.user_id?.takeIf { it.isNotEmpty() }?.let { _ ->
                        // Getting the user preferences
                        getUserConfigurationFromFirebase(userData)?.run {
                            if (registered_table_number != null) {
                                // Saving userData to Room
                                saveUserProfileToRoom(userData.toUserProfileDto())
                                // Saving userConfigurationToRoom
                                saveUserConfigurationToRoom(toUserConfigurationDto())

                                preferencesManager
                                    .saveInt(TABLE_NUMBER, registered_table_number)
                                // Updating the state
                                mutableUserRegistrationUiState.update {
                                    it.copy(
                                        isLoading = false,
                                        userDataDto = userData.toUserProfileDto()
                                    )
                                }
                            } else {
                                throw IllegalArgumentException("Registered Table number is null")
                            }
                        }
                    } ?: throw IllegalArgumentException("user id is empty")
                } ?: throw IllegalArgumentException("email is empty")
            } ?: run {
                registerUserDataToFireStore(
                    userData = loginResult.data,
                    onSuccess = { userDataDto, userConfigurationDto ->
                        saveUserProfileToRoom(userDataDto)
                        saveUserConfigurationToRoom(userConfigurationDto)
                        mutableUserRegistrationUiState.update {
                            it.copy(
                                isLoading = false,
                                userDataDto = userDataDto
                            )
                        }
                    }
                )
            }
        }
    }


    private suspend fun getUserConfigurationFromFirebase(userData: UserProfile): UserConfiguration? {
        return try {
            userData.email?.let { email ->
                userData.user_id?.let { userId ->
                    fireStore.collection(Constants.UserConfigurationPath.USERS.name)
                        .document(email)
                        .collection(Constants.UserConfigurationPath.USER_CONFIGURATION.name)
                        .document(userId)
                        .get()
                        .await()
                        .toObject(UserConfiguration::class.java)
                } ?: throw IllegalArgumentException()
            } ?: throw IllegalArgumentException()
        } catch (e: Exception) {
            throw e
        }
    }

    private suspend fun registerUserDataToFireStore(
        userData: UserProfile,
        onSuccess: suspend (UserProfileDto, UserConfigurationDto) -> Unit
    ) {
        val generatedTableNumber: Int = Random.nextInt(0, 1000)
        val userConfigurationStats = UserConfiguration(
            user_id = userData.user_id,
            premium_user = false,
            free_sessions = 3,
            registered_table_number = generatedTableNumber
        )


        withContext(Dispatchers.IO) {
            fireStore.collection(Constants.UserProfilePath.USERS.name)
                .document(userData.email ?: throw IllegalArgumentException())
                .collection(Constants.UserProfilePath.USER_DATA.name)
                .document(userData.user_id ?: throw IllegalArgumentException())
                .set(userData)
                .await()

            fireStore.collection(Constants.UserConfigurationPath.USERS.name)
                .document(userData.email)
                .collection(Constants.UserConfigurationPath.USER_CONFIGURATION.name)
                .document(userData.user_id)
                .set(userConfigurationStats)
                .await()

            preferencesManager
                .saveInt(TABLE_NUMBER, generatedTableNumber)

//            withContext(Dispatchers.IO) {
//                sendEmail(
//                    to = "bharadwajbharath890@gmail.com",
//                    subject = "Welcome to Insighify",
//                    messageBody = "Welcome to insightify.",
//                    from = "bharadwaj.rns@gmail.com",
//                    password = "rlql fdgw nply jytt"
//                )
//            }
        }
        onSuccess(
            userData.toUserProfileDto(),
            userConfigurationStats.toUserConfigurationDto()
        )
    }

    private suspend fun saveUserProfileToRoom(userDataDto: UserProfileDto) {
        withContext(Dispatchers.IO) {
            with(userDataDto) {
                userProfileDao.saveUserProfile(
                    UserProfileEntity(
                        userID = userId!!,
                        username = username,
                        email = email,
                        profileUrl = profileUrl,
                        registeredOn = registeredOn
                    )
                ).also { result ->
                    if (result > 0) {
                        Timber.tag("LOGIN_VM").i("user profile inserted to room")
                    } else {
                        Timber.tag("LOGIN_VM").e("user profile insertion failed")
                    }
                }
            }
        }
    }

    private suspend fun saveUserConfigurationToRoom(userConfigurationDto: UserConfigurationDto) {
        withContext(Dispatchers.IO) {
            with(userConfigurationDto) {
                userConfigurationDao.saveUserConfiguration(
                    userConfigurationEntity = UserConfigurationEntity(
                        userID = userId!!,
                        freeSessions = freeSessions,
                        premiumUser = premiumUser,
                        registeredTableNumber = registeredTableNumber
                    )
                ).also { result ->
                    if (result > 0) {
                        Timber.tag("LOGIN_VM").i("user configuration inserted to room")
                    } else {
                        Timber.tag("LOGIN_VM").e("user configuration insertion failed")
                    }
                }
            }
        }
    }

}