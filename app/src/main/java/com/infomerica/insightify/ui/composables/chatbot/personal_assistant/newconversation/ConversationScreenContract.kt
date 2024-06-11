package com.infomerica.insightify.ui.composables.chatbot.personal_assistant.newconversation

import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.ConversationModel
import com.infomerica.insightify.ui.composables.home.recentsessions.RecentSessionModel


/**
 * Events for New conversation.
 */
sealed class ConversationEvent {
    data class ExecuteQuery(val query: String) : ConversationEvent()
    data class SaveConversationToFirebase(val recentSessionModel: RecentSessionModel) :
        ConversationEvent()

    data class AddMessageToConversation(val conversationModel: ConversationModel) :
        ConversationEvent()

}


/**
 * Used for new conversation repose
 * from API
 */
data class ConversationResponseUiState(
    val isLoading: Boolean = false,
    val queryResponse: String? = null,
    val error: String = ""
)
/**
 * Used for new conversation saving to
 * Firebase.
 */
data class ConversationSavingUiState(
    val isSaving: Boolean = false,
    val saveCompleted: Boolean = false,
    val error: String = ""
)

