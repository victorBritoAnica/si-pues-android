plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.jetbrainsKotlinSerialization)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

hilt {
    enableAggregatingTask = false
}

android {
    namespace = "com.sipues"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sipues"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += listOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/AL2.0",
                "META-INF/LGPL2.1",
                "META-INF/LICENSE-notice.md"
            )
        }
    }
}

dependencies {
    // ===== [ CORE ANDROID ] =====
    implementation(libs.androidx.core.ktx)              // Kotlin extensions
    implementation(libs.androidx.lifecycle.runtime.ktx) // Lifecycle
    implementation(libs.androidx.activity.compose)      // Compose Activity
    implementation(libs.security.crypto)               // Encrypted SharedPrefs

    // ===== [ COMPOSE ] =====
    implementation(platform(libs.androidx.compose.bom)) // BOM para versiones
    implementation(libs.androidx.ui)                    // Compose UI
    implementation(libs.androidx.ui.graphics)           // Graphics
    implementation(libs.androidx.ui.tooling.preview)    // Preview
    implementation(libs.androidx.material3)             // Material3
    implementation(libs.androidx.compose.material.icons.extended) // Icons
    implementation(libs.androidx.lifecycle.runtime.compose) // Compose + Lifecycle
    implementation(libs.androidx.navigation.compose)    // Navigation

    // ===== [ NETWORKING ] =====
    implementation(libs.retrofit)                       // Retrofit
    implementation(libs.retrofit.converter.gson)        // Gson converter
    implementation(libs.okhttp)                         // OkHttp
    implementation(libs.okhttp.logging.interceptor)     // Logging interceptor
    implementation(libs.kotlinx.serialization.json)     // Kotlin Serialization

    // ===== [ DATABASE & PERSISTENCE ] =====
    implementation(libs.androidx.room.runtime)          // Room
    implementation(libs.androidx.room.ktx)              // Room Coroutines
    ksp(libs.androidx.room.compiler)                    // Room compiler
    implementation(libs.androidx.datastore)             // DataStore (Preferences)

    // ===== [ DI (HILT) ] =====
    implementation(libs.hilt.android)                   // Hilt
    implementation(libs.androidx.hilt.navigation.compose) // Hilt + Compose
    ksp(libs.hilt.compiler)                             // Hilt compiler

    // ===== [ IMAGES ] =====
    implementation(libs.coil.compose)                   // Coil para imágenes

    // ===== [ UTILITIES ] =====
    implementation(libs.accompanist.permissions)        // Permisos
    implementation(libs.play.services.location)         // Location
    implementation(libs.androidx.tools.core)            // Android Tools (opcional)

    // ===== [ TESTING - UNIT ] =====
    testImplementation(libs.junit)                      // JUnit
    testImplementation(libs.mockk)                      // MockK (para mocks estándar)
    testImplementation(libs.coroutines.test)            // Coroutines testing

    // ===== [ TESTING - ANDROID ] =====
    androidTestImplementation(libs.androidx.test.junit) // JUnit Ext
    androidTestImplementation(libs.mockk.android)              // MockK para Android
    androidTestImplementation(libs.androidx.espresso.core) // Espresso
    androidTestImplementation(libs.kakao)               // Kakao (DSL para Espresso)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.ui.test)
    // ===== [ DEBUG ] =====
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}