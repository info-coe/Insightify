package com.infomericainc.insightify.ui.composables.genericassistant.order

import androidx.annotation.Keep
import kotlinx.serialization.SerialName

@Keep
data class Orders(
    @SerialName("OrderData") val OrderData: OrderData
)