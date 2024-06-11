package com.infomerica.insightify.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.infomerica.insightify.db.entites.RecentSessionEntity
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.ConversationModel
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSessionDao {

    @Query("SELECT * FROM ${RecentSessionEntity.TABLE_NAME} WHERE session_id = :sessionId ")
    fun getRecentSessionData(sessionId : String) : Flow<RecentSessionEntity>

    @Query("SELECT ${RecentSessionEntity.SESSION_ID} FROM ${RecentSessionEntity.TABLE_NAME}")
    fun getRecentSessionIds() : Flow<List<String>>

    @Query("SELECT COUNT(*) FROM ${RecentSessionEntity.TABLE_NAME}")
    suspend fun getRowCount(): Int

    @Insert(entity = RecentSessionEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRecentSessionData(recentSessionEntity: RecentSessionEntity) : Long

    @Query("UPDATE ${RecentSessionEntity.TABLE_NAME} SET conversation = :updatedConversation WHERE session_id = :sessionId")
    suspend fun updateRecentSessionField(sessionId: String, updatedConversation : List<ConversationModel>) : Int

}