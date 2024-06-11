package com.infomerica.insightify.ui.activites

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.BetaOpenAI
import com.google.firebase.database.DatabaseReference
import com.infomerica.insightify.manager.OpenAiManager
import com.infomerica.insightify.util.Constants.APP_DATA
import com.infomerica.insightify.util.Constants.OPEN_AI_API_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class InsightifyViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val firebaseDatabase: DatabaseReference,
    private val openAiManager: OpenAiManager
): ViewModel() {

    val getApiKey = savedStateHandle.getStateFlow(OPEN_AI_API_KEY,"")

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable)
    }

    init {
        Timber.tag(INSIGHTIFY_VM)
            .i("Created")
    }

    fun onInsightifyEvents(
        event: InsightifyEvent
    ) {
        when(event) {
            is InsightifyEvent.FetchSystemKeys -> fetchSystemKeysFromFirebase()
        }
    }

    private fun fetchSystemKeysFromFirebase() {
        viewModelScope.launch(exceptionHandler) {
            firebaseDatabase
                .child(APP_DATA)
                .child(OPEN_AI_API_KEY)
                .get()
                .await()?.let { dataSnapshot ->
                    dataSnapshot.takeIf { it.exists() }?.let { validSnapShot ->
                        val apiKey = validSnapShot.value as? String
                        apiKey?.takeIf { it.isNotEmpty() }?.let { api_key ->
                            savedStateHandle[OPEN_AI_API_KEY] = ""
                            openAiManager.updateApiKey(apiKey)
                            Timber.tag(INSIGHTIFY_VM)
                                .i("API_KEY : $api_key - fetched")
                        }
                    }
                }
        }
    }
    override fun onCleared() {
        super.onCleared()
        Timber.tag(INSIGHTIFY_VM)
            .i("Cleared")
    }
}

private const val INSIGHTIFY_VM = "insightify_vm"