package com.infomericainc.insightify.ui.composables.shared

sealed class SharedEvent {
    data class SaveRecentSessionId(
        val id : String
    ) : SharedEvent()

}