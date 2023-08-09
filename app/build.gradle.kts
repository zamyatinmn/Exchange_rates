import java.util.*

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    kotlin("plugin.serialization")
}

android {
    namespace = "net.zamyatinmn.exchangerates"
    compileSdk = ConfigData.COMPILE_SDK_VERSION
    android.buildFeatures.buildConfig = true

    defaultConfig {
        applicationId = "net.zamyatinmn.exchangerates"
        minSdk = ConfigData.MIN_SDK_VERSION
        targetSdk = ConfigData.TARGET_SDK_VERSION
        versionCode = ConfigData.VERSION_CODE
        versionName = ConfigData.VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        val properties = Properties().apply {
            load(rootProject.file("apikey.properties").reader())
        }
        val key = properties["API_KEY"]

        buildConfigField("String", "API_KEY", key.toString())
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(Dependencies.AndroidX.CORE_KTX)
    implementation(Dependencies.AndroidX.LIFECYCLE)
    implementation(Dependencies.AndroidX.ACTIVITY)
    implementation(Dependencies.AndroidX.COROUTINES)

    implementation(platform(Dependencies.Compose.PLATFORM))
    implementation(Dependencies.Compose.UI)
    implementation(Dependencies.Compose.UI_GRAPHICS)
    implementation(Dependencies.Compose.PREVIEW)
    implementation(Dependencies.Compose.MATERIAL)


    implementation(Dependencies.Compose.NAVIGATION)

    implementation(Dependencies.Room.CORE)
    implementation(Dependencies.Room.COROUTINES)
    kapt(Dependencies.Room.COMPILER)

    implementation(Dependencies.Hilt.CORE)
    kapt(Dependencies.Hilt.COMPILER)

    implementation(Dependencies.Retrofit.CORE)
    implementation(Dependencies.Retrofit.KOTLINX_CONVERTER)

    implementation(Dependencies.OKHTTP)

    implementation(Dependencies.KOTLINX_SERIALIZATION)

    testImplementation(Dependencies.JUNIT)
    androidTestImplementation(Dependencies.AndroidX.JUNIT)
    androidTestImplementation(Dependencies.ESPRESSO)
    androidTestImplementation(platform(Dependencies.Compose.PLATFORM))
    androidTestImplementation(Dependencies.Compose.JUNIT)
    debugImplementation(Dependencies.Compose.UI_TOOLING)
    debugImplementation(Dependencies.Compose.TEST_MANIFEST)
}

kapt {
    correctErrorTypes = true
}