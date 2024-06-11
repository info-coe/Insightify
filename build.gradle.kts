// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        maven (
            url="https://jitpack.io"
        )
        maven (
            url = "https://phonepe.mycloudrepo.io/public/repositories/phonepe-intentsdk-android"
        )
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48")
    }
}
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
    id("com.android.library") version "8.2.0" apply false
    id("org.jetbrains.kotlin.jvm") version "1.8.10" apply false
    id ("org.jetbrains.kotlin.plugin.serialization") version "1.6.21" apply false
}