package com.infomericainc.insightify.ui.composables.generic_assistant.menu

import androidx.annotation.Keep

@Keep
data class MenuData(
    val items: List<Item?>? = null,
    val menuCategory: String? = null
)