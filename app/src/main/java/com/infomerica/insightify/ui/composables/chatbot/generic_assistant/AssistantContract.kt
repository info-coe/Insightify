package com.infomerica.insightify.ui.composables.chatbot.generic_assistant

import android.app.Activity
import android.content.Context
import com.aallam.openai.api.BetaOpenAI
import com.infomerica.insightify.ui.composables.chatbot.generic_assistant.components.AssistantConversationModel
import com.infomerica.insightify.ui.composables.chatbot.generic_assistant.order.Orders
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener

sealed class AssistantEvent {
    data object GetPreviousConversationFromAssistant : AssistantEvent()
    data class GetResponseFromAssistant(val message: String) : AssistantEvent()
    data class AddMessageToConversation(val conversationModel: AssistantConversationModel) :
        AssistantEvent()

    data class MakePaymentFromGooglePay(
        val activity: Activity,
        val amount : String,
        val upi : String,
        val name : String,
        val description : String,
        val transactionID : String,
        val transactionRefID : String,
        val paymentStatusListener: PaymentStatusListener,
        val context: Context,
    ) : AssistantEvent()

    data class AddOrdersToFirebase(val orders: Orders?) : AssistantEvent()

}

data class AssistantResponseUiState(
    val isLoading: Boolean = false,
    val assistantResponse: String? = null,
    val error: String? = null
)

@OptIn(BetaOpenAI::class)
data class PreviousConversationUiState(
    val isLoading: Boolean = false,
    val previousConversation: List<AssistantConversationModel>? = null,
    val emptyConversation: Boolean? = null
)


data class PaymentUIState(
    val amount: Int,
    val upi: String,
    val name: String,
    val description: String,
    val transactionID: String,
    val paymentStatusListener: PaymentStatusListener,
    val context: Context,

    )