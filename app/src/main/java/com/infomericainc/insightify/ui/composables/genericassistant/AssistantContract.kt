package com.infomericainc.insightify.ui.composables.genericassistant

import com.infomericainc.insightify.ui.composables.genericassistant.order.Orders

sealed class AssistantEvent {
    data object GetPreviousConversationFromAssistant : AssistantEvent()
    data class GetResponseFromAssistant(val message: String) : AssistantEvent()
    data class AddMessageToConversation(val conversationModel: AssistantConversationModel) :
        AssistantEvent()
    data object DeleteConversationFromAssistant : AssistantEvent()
    data class AddOrdersToFirebase(val orders: Orders?) : AssistantEvent()

}

data class AssistantResponseUiState(
    val isLoading: Boolean = false,
    val assistantResponse: String? = null,
    val error: String? = null
)

data class PreviousConversationUiState(
    val isLoading: Boolean = false,
    val previousConversation: List<AssistantConversationModel>? = null,
    val emptyConversation: Boolean? = null
)

data class ConversationDeletionUiState(
    val isDeleting: Boolean = false,
    val deleted: Boolean = false,
    val error: String? = null
)

data class ConversationRestrictionUIState(
    val isLimitReached : Boolean = false
)
