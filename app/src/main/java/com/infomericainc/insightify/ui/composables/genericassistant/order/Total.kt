package com.infomericainc.insightify.ui.composables.genericassistant.order


@androidx.annotation.Keep
data class Total(
    val Amount: Double,
    val GST: Double,
    val GrandTotal: Double
)