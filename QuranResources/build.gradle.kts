plugins {
    id("com.android.dynamic-feature")
}
android {
    namespace = "mohalim.islamic.alarm.alert.quranresources"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(project(":app"))
}