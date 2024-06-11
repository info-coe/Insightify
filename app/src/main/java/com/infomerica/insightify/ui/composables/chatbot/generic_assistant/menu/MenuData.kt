package com.infomerica.insightify.ui.composables.chatbot.generic_assistant.menu

import androidx.annotation.Keep

@Keep
data class MenuData(
    val items: List<Item?>? = null,
    val menuCategory: String? = null
)