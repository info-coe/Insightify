package com.infomerica.insightify.ui.composables.chatbot.generic_assistant.order


@androidx.annotation.Keep
data class Total(
    val Amount: Double,
    val GST: Double,
    val GrandTotal: Double
)