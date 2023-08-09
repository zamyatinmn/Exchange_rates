
object Dependencies {
    const val DESUGARING = "com.android.tools:desugar_jdk_libs:1.1.5"
    const val JUNIT = "junit:junit:4.13.2"
    const val ESPRESSO = "androidx.test.espresso:espresso-core:3.5.1"
    const val KOTLINX_SERIALIZATION = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1"
    const val OKHTTP = "com.squareup.okhttp3:okhttp:4.9.3"

    object Compose {
        const val PLATFORM = "androidx.compose:compose-bom:2023.03.00"
        const val VERSION = "1.4.3"
        const val UI = "androidx.compose.ui:ui"
        const val UI_GRAPHICS = "androidx.compose.ui:ui-graphics"
        const val MATERIAL = "androidx.compose.material3:material3"
        const val NAVIGATION = "androidx.navigation:navigation-compose:2.6.0"

        const val PREVIEW = "androidx.compose.ui:ui-tooling-preview"

        const val JUNIT = "androidx.compose.ui:ui-test-junit4"
        const val UI_TOOLING = "androidx.compose.ui:ui-tooling"
        const val TEST_MANIFEST = "androidx.compose.ui:ui-test-manifest"
    }

    object Room {
        const val VERSION = "2.4.2"
        const val CORE = "androidx.room:room-common:$VERSION"
        const val COMPILER = "androidx.room:room-compiler:$VERSION"
        const val COROUTINES = "androidx.room:room-ktx:$VERSION"
    }

    object Hilt {
        const val VERSION = "2.47"
        const val CORE = "com.google.dagger:hilt-android:$VERSION"
        const val COMPILER = "com.google.dagger:hilt-android-compiler:$VERSION"
    }

    object AndroidX {
        const val CORE_KTX = "androidx.core:core-ktx:1.10.1"
        const val LIFECYCLE = "androidx.lifecycle:lifecycle-runtime-ktx:2.6.1"
        const val ACTIVITY = "androidx.activity:activity-compose:1.7.2"
        const val JUNIT = "androidx.test.ext:junit:1.1.3"
        const val COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9"
    }

    object Retrofit {
        const val VERSION = "2.9.0"
        const val CORE = "com.squareup.retrofit2:retrofit:$VERSION"
        const val KOTLINX_CONVERTER = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0"
    }
}

object ConfigData {
    const val COMPILE_SDK_VERSION = 33
    const val MIN_SDK_VERSION = 24
    const val TARGET_SDK_VERSION = 33
    const val VERSION_CODE = 1
    const val VERSION_NAME = "1.0"
}