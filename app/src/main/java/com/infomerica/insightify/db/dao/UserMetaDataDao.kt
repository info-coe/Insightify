package com.infomerica.insightify.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.infomerica.insightify.db.entites.UserMetaDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserMetaDataDao {

    @Query("SELECT * FROM ${UserMetaDataEntity.TABLE_NAME}")
    fun getUserMetaData() : Flow<UserMetaDataEntity>

    @Query("SELECT COUNT(*) FROM ${UserMetaDataEntity.TABLE_NAME}")
    suspend fun getRowCount(): Int

    @Insert(entity = UserMetaDataEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserMetaData(userMetaDataEntity: UserMetaDataEntity) : Long

    @Update(entity = UserMetaDataEntity::class)
    @Throws(Exception::class)
    suspend fun updateUserMetaData(userMetaDataEntity: UserMetaDataEntity)

}