package com.infomericainc.insightify.di

import com.infomericainc.insightify.api.StripeRepository
import com.infomericainc.insightify.api.StripeService
import com.infomericainc.insightify.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {


    @Provides
    @Singleton
    fun provideStripeRepository(
        stripeService: StripeService
    ): StripeRepository {
        return StripeRepository(
            stripeService
        )
    }

    @Singleton
    @Provides
    fun provideStripeService(
        retrofit: Retrofit
    ): StripeService {
        return retrofit
            .create(StripeService::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }


    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(loggingInterceptor)
            .retryOnConnectionFailure(true)
            .build()
    }


}