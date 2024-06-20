package com.infomerica.insightify.di

import android.content.Context
import androidx.room.Room
import com.infomerica.insightify.db.InsightifyDatabase
import com.infomerica.insightify.db.dao.UserConfigurationDao
import com.infomerica.insightify.db.dao.UserMetaDataDao
import com.infomerica.insightify.db.dao.UserProfileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val DATABASE_NAME = "InsightifyDatabase"
    @Provides
    @Singleton
    fun provideRoomDatabase(
        @ApplicationContext context: Context
    ) : InsightifyDatabase {
        return Room.databaseBuilder(context,InsightifyDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserProfileDao(
        insightifyDatabase: InsightifyDatabase
    ) : UserProfileDao {
        return insightifyDatabase.userProfileDao()
    }

    @Provides
    @Singleton
    fun provideUserConfigurationDao(
        insightifyDatabase: InsightifyDatabase
    ) : UserConfigurationDao {
        return insightifyDatabase.userConfigurationDao()
    }

    @Provides
    @Singleton
    fun provideUserMetaDataDao(
        insightifyDatabase: InsightifyDatabase
    ) : UserMetaDataDao {
        return insightifyDatabase.userMetaDataDao()
    }



}