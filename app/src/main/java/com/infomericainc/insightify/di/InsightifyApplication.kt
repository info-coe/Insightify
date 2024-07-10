package com.infomericainc.insightify.di

import android.app.Application
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.stripe.android.PaymentConfiguration
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class InsightifyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Firebase.initialize(this)

        FirebaseAppCheck.getInstance()
            .installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance(),
                true
            )

        PaymentConfiguration.init(this, "pk_test_51PWGJQ03rPs1nuQKTcmQrSqontAYJCilYtFylukxpXoWrCnCaFRXldvxCWvlgiMLHejqojL9hp6xhrmTHIxB1S9U00VQbaSA1T")

        Timber.plant(Timber.DebugTree())
    }
}