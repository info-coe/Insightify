package com.infomericainc.insightify.ui.composables.genericassistant.menu

import androidx.annotation.Keep


@Keep
data class Item(
    val GST: Double? = null,
    val amount: Double? = null,
    val itemDescription: String? = null,
    val itemName: String? = null,
    val itemNumber: Int? = null,
    val itemOfTheDay: Boolean? = null,
    val spiceLevel: Int? = null
)