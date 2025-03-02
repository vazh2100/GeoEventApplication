plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
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
    // Compose Bom
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    // Compose Not Bom
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    // KoinDI
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    // JSON
    implementation(libs.kotlinx.serialization.json)
    // modules
    implementation(project(":1_core"))
    implementation(project(":1_core_a"))
    implementation(project(":1_network"))
    implementation(project(":1_geolocation"))
    implementation(project(":1_theme"))
    implementation(project(":2_events"))
}
