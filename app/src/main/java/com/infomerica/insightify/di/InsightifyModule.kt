package com.infomerica.insightify.di

import com.infomerica.insightify.manager.DateGenerationManager
import com.infomerica.insightify.manager.MD5Manager
import com.infomerica.insightify.manager.OpenAiManager
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