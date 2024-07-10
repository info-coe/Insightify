package com.infomericainc.insightify.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.infomericainc.insightify.db.entites.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {


    @Query("SELECT * FROM ${UserProfileEntity.TABLE_NAME}")
    fun getUserProfile() : Flow<UserProfileEntity>

    @Query("SELECT COUNT(*) FROM ${UserProfileEntity.TABLE_NAME}")
    suspend fun getRowCount(): Int

    @Insert(entity = UserProfileEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProfile(userProfileEntity: UserProfileEntity) : Long

    @Update(entity = UserProfileEntity::class)
    @Throws(Exception::class)
    suspend fun updateUserProfile(userProfileEntity: UserProfileEntity)

}