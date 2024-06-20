package com.infomerica.insightify.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.infomerica.insightify.db.dao.UserConfigurationDao
import com.infomerica.insightify.db.dao.UserMetaDataDao
import com.infomerica.insightify.db.dao.UserProfileDao
import com.infomerica.insightify.db.entites.UserConfigurationEntity
import com.infomerica.insightify.db.entites.UserMetaDataEntity
import com.infomerica.insightify.db.entites.UserProfileEntity


@Database(
    entities = [UserProfileEntity::class,UserConfigurationEntity::class,UserMetaDataEntity::class],
    version = 1,
    exportSchema = true
)
abstract class InsightifyDatabase : RoomDatabase() {

    abstract fun userProfileDao() : UserProfileDao

    abstract fun userConfigurationDao() : UserConfigurationDao

    abstract fun userMetaDataDao() : UserMetaDataDao

}