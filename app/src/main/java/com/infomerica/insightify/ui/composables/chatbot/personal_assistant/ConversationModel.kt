package com.infomerica.insightify.ui.composables.chatbot.personal_assistant

import android.os.Parcelable
import com.google.errorprone.annotations.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ConversationModel(
    val message: String = "",
    val conversationType: ConversationType = ConversationType.TEXT,
    val messageType: MessageType = MessageType.STATIC,
    val messageOrigin: MessageOrigin = MessageOrigin.ASSISTANT,
    val responseType: ResponseType = ResponseType.DEFAULT,
    val dropDownItem: DropDownItem = DropDownItem(),
) : Parcelable

@Keep
@Parcelize
enum class ConversationType : Parcelable {
    TEXT,
    OPTION,
    NUMBER
}

@Keep
enum class ResponseType {
    DEFAULT,
    DROPDOWN,
}

@Keep
enum class MessageType {
    STATIC,
    GENERATED
}

@Keep
enum class MessageOrigin {
    USER,
    ASSISTANT
}

@Keep
@Parcelize
data class DropDownItem(
    @SerializedName("question")
    val question: String = "",

    @SerializedName("answer")
    val answer: String = "",

    @SerializedName("codeBlock")
    val codeBlock : String = "",

    @SerializedName("referenceText")
    val referenceText: String = "",

    @SerializedName("referenceUrl")
    val referenceUrl: String = ""
) : Parcelable

val DEMO_CONVERSATION : List<ConversationModel> = listOf(
    ConversationModel(
        message = "Hey ! how you doing today.",
        conversationType = ConversationType.TEXT,
        messageOrigin = MessageOrigin.USER,
        messageType = MessageType.STATIC,
        responseType = ResponseType.DEFAULT
    ),
    ConversationModel(
        message = "Fine what about you. How is your health.",
        conversationType = ConversationType.TEXT,
        messageOrigin = MessageOrigin.ASSISTANT,
        messageType = MessageType.GENERATED,
        responseType = ResponseType.DEFAULT
    ),
    ConversationModel(
        message = "Yeah, i am still recovering, Doctor told me to take at least 3 days bed rest.",
        conversationType = ConversationType.TEXT,
        messageOrigin = MessageOrigin.USER,
        messageType = MessageType.STATIC,
        responseType = ResponseType.DEFAULT
    ),
    ConversationModel(
        message = "Good to hear about that.",
        conversationType = ConversationType.TEXT,
        messageOrigin = MessageOrigin.ASSISTANT,
        messageType = MessageType.GENERATED,
        responseType = ResponseType.DEFAULT
    ),
)