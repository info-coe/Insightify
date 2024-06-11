package com.infomerica.insightify.db.entites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.errorprone.annotations.Keep
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.ConversationModel

@Entity(
    tableName = RecentSessionEntity.TABLE_NAME
)
@Keep
data class RecentSessionEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = SESSION_ID) val id : String = "",
    @ColumnInfo(name = TITLE) val title : String? = null,
    @ColumnInfo(name = LAST_SEEN) val lastSeen : String? = null,
    @ColumnInfo(name = STARRED) val starred : Boolean? = null,
    @ColumnInfo(name = CONVERSATION) val conversation : List<ConversationModel>? = null
) {
    companion object {
        const val TABLE_NAME = "recent_session"
        const val SESSION_ID = "session_id"
        const val TITLE = "title"
        const val LAST_SEEN = "last_seen"
        const val STARRED = "starred"
        const val CONVERSATION = "conversation"
    }
}
