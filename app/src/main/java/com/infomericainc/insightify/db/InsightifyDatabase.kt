package com.infomericainc.insightify.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.infomericainc.insightify.db.dao.UserConfigurationDao
import com.infomericainc.insightify.db.dao.UserProfileDao
import com.infomericainc.insightify.db.entites.UserConfigurationEntity
import com.infomericainc.insightify.db.entites.UserProfileEntity


@Database(
    entities = [UserProfileEntity::class, UserConfigurationEntity::class],
    version = 2,
    exportSchema = true
)
abstract class InsightifyDatabase : RoomDatabase() {

    abstract fun userProfileDao(): UserProfileDao

    abstract fun userConfigurationDao(): UserConfigurationDao

}