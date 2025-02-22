// Top-le vel build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        // JitPack repositoryni qo'shish
        maven(url = "https://jitpack.io")
    }
    dependencies {
        classpath(libs.androidx.navigation.safe.args.gradle.plugin)
    }
}
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("com.android.library") version "8.0.0" apply false
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
}