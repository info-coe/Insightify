package com.infomerica.insightify.ui.composables.chatbot.generic_assistant.order

import androidx.annotation.Keep
import kotlinx.serialization.SerialName

@Keep
data class Orders(
    @SerialName("OrderData") val OrderData: OrderData
)