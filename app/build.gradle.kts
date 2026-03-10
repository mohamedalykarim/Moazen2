plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id ("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "mohalim.islamic.alarm.alert.moazen"
    compileSdk = 36

    defaultConfig {
        applicationId = "mohalim.islamic.alarm.alert.moazen"
        minSdk = 26
        targetSdk = 36
        versionCode = 24
        versionName = "1.24"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures{
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    implementation("androidx.core:core-ktx:1.17.0")

    val composeBom = platform("androidx.compose:compose-bom:2026.02.01")
    implementation(composeBom)
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:1.12.4")
    implementation("androidx.compose.animation:animation-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    implementation ("androidx.datastore:datastore-preferences:1.1.2")

    implementation ("com.google.dagger:hilt-android:2.55")
    kapt ("com.google.dagger:hilt-compiler:2.55")
    implementation("androidx.hilt:hilt-work:1.2.0")
    kapt("androidx.hilt:hilt-compiler:1.2.0")

    implementation ("com.google.code.gson:gson:2.11.0")

    implementation("androidx.work:work-runtime-ktx:2.11.1")

    implementation ("androidx.room:room-runtime:2.8.4")
    kapt ("androidx.room:room-compiler:2.8.4")
    implementation ("androidx.room:room-ktx:2.8.4")

    implementation ("com.google.android.play:feature-delivery-ktx:2.1.0")

    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
}

kapt{
    correctErrorTypes = true
}
