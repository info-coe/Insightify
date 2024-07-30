package com.infomericainc.insightify.ui.composables.threads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.infomericainc.insightify.db.dao.UserProfileDao
import com.infomericainc.insightify.db.entites.UserProfileEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ThreadsViewModel @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val userProfileDao: UserProfileDao,
    private val firebaseCrashlytics: FirebaseCrashlytics
) : ViewModel() {

    private val mutableThreadsUIState = MutableStateFlow(ThreadsUIState())
    val threadsUiState = mutableThreadsUIState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        firebaseCrashlytics.recordException(throwable)
    }

    fun onEvent(event: ThreadsEvent) {
        when(event) {
            is ThreadsEvent.FetchThreadsFromFirebase -> fetchThreadsFromFirebase()
        }
    }

    private suspend fun getUserProfile(): UserProfileEntity? {
        return userProfileDao
            .getUserProfile()
            .firstOrNull()
            .also {
                Timber
                    .tag(THREADS_VIEW_MODEL)
                    .e("User Email from Room : ${it?.email}")
            }
    }

    private fun fetchThreadsFromFirebase() {
        mutableThreadsUIState
            .update {
                it.copy(
                    isFetching = true
                )
            }
        viewModelScope.launch(exceptionHandler) {
            val userProfileEntity = getUserProfile() ?: return@launch updateThreadsErrorUiState("Unable to get User Data.")
            val email = userProfileEntity.email ?: return@launch updateThreadsErrorUiState("Unable to get email.")
            getThreadsFromFirebase(email).run {
                if(isNotEmpty()) {
                    mutableThreadsUIState
                        .update {
                            it.copy(
                                isFetching = false,
                                fetched = this
                            )
                        }
                } else {
                    updateThreadsErrorUiState("Unable to get data documents are empty.")
                }
            }
        }
    }

    private fun updateThreadsErrorUiState(error : String) {
        mutableThreadsUIState
            .update {
                it.copy(
                    isFetching = false,
                    error = error
                )
            }
    }

    private suspend fun getThreadsFromFirebase(email: String) : List<ThreadItemDto?> = withContext(Dispatchers.IO) {
        fireStore
            .collection("USERS")
            .document(email)
            .collection("THREAD_POOL")
            .get()
            .await()
            .documents
            .mapNotNull {
                it.toObject(ThreadItemDto::class.java)
            }
    }

}

private const val THREADS_VIEW_MODEL = "ThreadsViewModel"