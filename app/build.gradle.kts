import org.gradle.kotlin.dsl.android

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
}

android {
    val getVersionName: (Int) -> String = { versionCode ->
        val major = versionCode / 100 + 1
        val minor = (versionCode % 100) / 10
        val patch = versionCode % 10
        "$major.$minor.$patch"
    }

    namespace = "com.vazh2100.geoeventapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.vazh2100.geoeventapp"
        minSdk = 26
        targetSdk = 35
        versionCode = 4
        versionName = getVersionName(versionCode!!)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file(System.getenv("KEYSTORE_PATH") ?: "keystore.jks")
            storePassword = System.getenv("KEYSTORE_PASSWORD") ?: ""
            keyAlias = System.getenv("KEY_ALIAS") ?: ""
            keyPassword = System.getenv("KEY_PASSWORD") ?: ""
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    applicationVariants.configureEach {
        outputs.configureEach {
            // Change output file name to match the desired naming convention
            val outputImpl = this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
            val newApkName = "${defaultConfig.applicationId}.v${defaultConfig.versionName}.apk"
            outputImpl.outputFileName = newApkName
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // Core
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    // Navigation
    implementation(libs.androidx.navigation.compose)
    // KoinDI
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    // JSON
    implementation(libs.kotlinx.serialization.json)
    // Room
    runtimeOnly(libs.room.runtime)
    ksp(libs.room.compiler)
    // Location Google Play Services
    implementation(libs.play.services.location)
    // Tests
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.ui.tooling)
    debugRuntimeOnly(libs.androidx.ui.test.manifest)
    // modules
    implementation(project(":core"))
    implementation(project(":feature_events"))
}
