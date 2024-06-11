package com.infomerica.insightify.ui.composables.shared

sealed class SharedEvent {
    data class SaveRecentSessionId(
        val id : String
    ) : SharedEvent()

}