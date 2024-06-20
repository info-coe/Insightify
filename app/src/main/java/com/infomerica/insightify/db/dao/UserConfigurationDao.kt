package com.infomerica.insightify.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.infomerica.insightify.db.entites.UserConfigurationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserConfigurationDao {


    @Query("SELECT * FROM ${UserConfigurationEntity.TABLE_NAME}")
    fun getUserConfiguration() : Flow<UserConfigurationEntity>

    @Insert(entity = UserConfigurationEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserConfiguration(userConfigurationEntity: UserConfigurationEntity) : Long

    @Update(entity = UserConfigurationEntity::class)
    @Throws(Exception::class)
    suspend fun updateUserConfiguration(userConfigurationEntity: UserConfigurationEntity)

}