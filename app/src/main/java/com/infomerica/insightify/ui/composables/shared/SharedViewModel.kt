package com.infomerica.insightify.ui.composables.shared

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        Timber.tag(SHARED_VM).d("created")
    }

    override fun onCleared() {
        super.onCleared()
        Timber.tag(SHARED_VM).d("cleared")
    }
    fun onTriggerEvent(event: SharedEvent) {
        when (event) {
            is SharedEvent.SaveRecentSessionId -> saveRecentSessionId(event.id)
        }
    }

    private fun saveRecentSessionId(id: String) {
        savedStateHandle[SESSION_KEY] = id
    }

    fun getRecentSessionId(): StateFlow<String> {
        return savedStateHandle.getStateFlow(SESSION_KEY,"")
    }
}

private const val SESSION_KEY = "session_key"
private const val SHARED_VM = "Shared VM"