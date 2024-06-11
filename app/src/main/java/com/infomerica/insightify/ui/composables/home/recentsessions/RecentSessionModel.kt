package com.infomerica.insightify.ui.composables.home.recentsessions

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.ConversationModel
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class RecentSessionModel(
    val title: String = "",
    val lastSeen: String = "",
    val starred: Boolean = false,
    val sessionId: String = "",
    val serverTimestamp: Timestamp = Timestamp.now(),
    val conversation : List<ConversationModel> = listOf()
) : Parcelable {
    companion object {
        val demoData = listOf(
            RecentSessionModel(
                title = "Kotlin",
                lastSeen = "16/01",
                starred = true
            ),
            RecentSessionModel(
                title = "Python",
                lastSeen = "15/01",
                starred = false
            ),
        )
    }
}


data class RecentSessionListModel(
    val recentSessionModel: RecentSessionModel = RecentSessionModel(),
    var isFavouriteLoading: Boolean = false
)
