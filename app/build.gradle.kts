import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
plugins {
    kotlin("kapt")
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = AppConfig.applicationId
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        testInstrumentationRunner = AppConfig.testInstrumentationRunner

        buildConfigField("String", "API_CLIENT_ID", gradleLocalProperties(rootDir).getProperty("API_CLIENT_ID"))
        buildConfigField("String", "API_CLIENT_SECRET", gradleLocalProperties(rootDir).getProperty("API_CLIENT_SECRET"))
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(Dependencies.Libraries.AndroidX)

    implementation(Dependencies.Libraries.Ktx)

    implementation(Dependencies.Libraries.Retrofit)

    implementation(Dependencies.Libraries.Okhttp)

    implementation(Dependencies.Libraries.Glide)
    kapt(Dependencies.Libraries.Glide)

    implementation(Dependencies.Libraries.Hilt)
    kapt(Dependencies.Libraries.Hilt)

    implementation(Dependencies.Libraries.Room)
    kapt(Dependencies.Libraries.Room)

    implementation(Dependencies.Libraries.Timber)

    testImplementation(Dependencies.Libraries.CoroutineTest)

    testImplementation(Dependencies.Libraries.Test)

    androidTestImplementation(Dependencies.Libraries.AndroidTest)
}