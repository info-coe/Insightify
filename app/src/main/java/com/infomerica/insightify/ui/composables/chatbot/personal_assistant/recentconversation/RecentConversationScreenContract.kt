package com.infomerica.insightify.ui.composables.chatbot.personal_assistant.recentconversation

import com.google.errorprone.annotations.Keep
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.ConversationModel


sealed class RecentConversationEvent {

    data class FetchRecentConversation(val sessionId: String) : RecentConversationEvent()
    data class ExecuteQuery(val query: String) : RecentConversationEvent()
    data class SaveConversationToFirebase(val sessionId: String) :
        RecentConversationEvent()

    data class AddMessageToConversation(val conversationModel: ConversationModel) :
        RecentConversationEvent()

}

@Keep
data class RecentConversationUIState(
    val isLoading: Boolean = false,
    val recentConversations : List<ConversationModel>? = null,
    val error: String = ""
)

@Keep
data class RecentConversationResponseUiState(
    val isLoading: Boolean = false,
    val queryResponse: String? = null,
    val error: String = ""
)

@Keep
data class RecentConversationSavingUiState(
    val isSaving: Boolean = false,
    val saveCompleted: Boolean = false,
    val error: String = ""
)

