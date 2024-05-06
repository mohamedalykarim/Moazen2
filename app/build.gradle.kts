plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id ("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "mohalim.islamic.alarm.alert.moazen"
    compileSdk = 34

    defaultConfig {
        applicationId = "mohalim.islamic.alarm.alert.moazen"
        minSdk = 26
        targetSdk = 33
        versionCode = 22
        versionName = "1.22"

//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures{
        compose = true
        buildConfig = true
    }

    composeOptions{
        kotlinCompilerExtensionVersion = "1.5.12"
    }


    dynamicFeatures += setOf(":Quran")
    buildToolsVersion = "34.0.0"
}

dependencies {
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    val composeBom = platform("androidx.compose:compose-bom:2023.10.01")
    implementation(composeBom)
//    androidTestImplementation(composeBom)
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.compose.animation:animation-graphics:1.6.6")

    implementation ("androidx.datastore:datastore-preferences:1.1.0")

    implementation ("com.google.dagger:hilt-android:2.49")
    kapt ("com.google.dagger:hilt-compiler:2.48")
    implementation("androidx.hilt:hilt-work:1.2.0")
    kapt("androidx.hilt:hilt-compiler:1.2.0")

    implementation ("com.google.code.gson:gson:2.10.1")

    implementation("androidx.work:work-runtime-ktx:2.9.0")


    implementation ("androidx.room:room-runtime:2.6.1")
    annotationProcessor ("androidx.room:room-compiler:2.6.1")

    // To use Kotlin annotation processing tool (kapt)
    kapt ("androidx.room:room-compiler:2.6.1")


    // Additional Resources
    implementation ("com.google.android.play:core:1.10.3")


    implementation ("com.squareup.retrofit2:retrofit:2.11.0")


}

kapt{
    correctErrorTypes = true
}