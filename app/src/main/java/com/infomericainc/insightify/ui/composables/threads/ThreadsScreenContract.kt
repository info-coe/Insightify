package com.infomericainc.insightify.ui.composables.threads

sealed class ThreadsEvent {
    data object FetchThreadsFromFirebase  : ThreadsEvent()

}

data class ThreadsUIState(
    val isFetching : Boolean = false,
    val fetched : List<ThreadItemDto?>? = null,
    val error : String? = null
)