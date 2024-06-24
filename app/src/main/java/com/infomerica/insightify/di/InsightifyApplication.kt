package com.infomerica.insightify.di

import android.app.Application
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class InsightifyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Firebase.initialize(context = this)

        FirebaseAppCheck.getInstance()
            .installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance(),
                true
            )

        Timber.plant(Timber.DebugTree())
    }
}