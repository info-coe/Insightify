package com.infomerica.insightify.di

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseFireStore() : FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    fun provideFirebaseRealTimeDatabase() : DatabaseReference = Firebase.database
        .getReferenceFromUrl("https://infogen-9d856-default-rtdb.asia-southeast1.firebasedatabase.app/")


}