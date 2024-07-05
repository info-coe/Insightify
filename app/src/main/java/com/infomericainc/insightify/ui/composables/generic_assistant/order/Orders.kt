package com.infomericainc.insightify.ui.composables.generic_assistant.order

import androidx.annotation.Keep
import kotlinx.serialization.SerialName

@Keep
data class Orders(
    @SerialName("OrderData") val OrderData: OrderData
)