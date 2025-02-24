plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.vazh2100.core"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    testOptions {
        unitTests.all {
            it.jvmArgs = listOf("-XX:+EnableDynamicAgentLoading")
        }
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    // KoinDI
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    // Json
    implementation(libs.kotlinx.serialization.json)
    // Room
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    // Location Google Play Services
    implementation(libs.play.services.location)
    // Tests
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
