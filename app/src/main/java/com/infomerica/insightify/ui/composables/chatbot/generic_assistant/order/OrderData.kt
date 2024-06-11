package com.infomerica.insightify.ui.composables.chatbot.generic_assistant.order

import androidx.annotation.Keep
import kotlinx.serialization.SerialName

@Keep
data class OrderData(
    @SerialName("Appetizers") val Appetizers: List<Appetizer>? = null,
    @SerialName("Desert") val Desert: List<Desert>? = null,
    @SerialName("Drinks") val Drinks: List<Drink>? = null,
    @SerialName("Main") val Main: List<Main>? = null,
    @SerialName("Soups") val Soups: List<Soup>? = null,
    @SerialName("Total") val Total: Total? = null
)