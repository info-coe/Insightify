package com.infomerica.insightify.ui.composables.chatbot.generic_assistant.components

import com.aallam.openai.api.BetaOpenAI
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class AssistantConversationModel(
    val message: String? = null,
    val isFromUser: Boolean = false
)
