package com.infomerica.insightify.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.ConversationModel

class ConversationModelListConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromChatModelList(conversationModelList: List<ConversationModel>): String {
        return gson.toJson(conversationModelList)
    }

    @TypeConverter
    fun toChatModelList(json: String): List<ConversationModel> {
        val type = object : TypeToken<List<ConversationModel>>() {}.type
        return gson.fromJson(json, type)
    }
}