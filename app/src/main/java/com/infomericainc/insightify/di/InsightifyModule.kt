package com.infomericainc.insightify.di

import com.infomericainc.insightify.manager.DateGenerationManager
import com.infomericainc.insightify.manager.MD5Manager
import com.infomericainc.insightify.manager.OpenAiManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InsightifyModule {
    @Singleton
    @Provides
    fun provideOpenApiManager() : OpenAiManager = OpenAiManager()

    @Singleton
    @Provides
    fun dateGenerationManager() : DateGenerationManager = DateGenerationManager()

    @Singleton
    @Provides
    fun md5Manager() : MD5Manager = MD5Manager()
}