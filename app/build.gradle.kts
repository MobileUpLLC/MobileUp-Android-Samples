plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.convetion.lint)
    alias(libs.plugins.compose.compiler)
}

android {
    val minSdkVersion = libs.versions.minSdk.get().toInt()
    val targetSdkVersion = libs.versions.targetSdk.get().toInt()
    val compileSdkVersion = libs.versions.compileSdk.get().toInt()

    namespace = "ru.mobileup.samples"
    compileSdk = compileSdkVersion

    androidResources {
        generateLocaleConfig = true
    }

    defaultConfig {
        applicationId = "ru.mobileup.samples"
        minSdk = minSdkVersion
        targetSdk = targetSdkVersion
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()
    }

    signingConfigs {
        getByName("debug") {
        }

        create("release") {
        }
    }

    buildTypes {
        debug {
            versionNameSuffix = "-debug"
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs["debug"]
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )
            signingConfig = signingConfigs["release"]
        }
    }

    setFlavorDimensions(listOf("backend"))
    productFlavors {
        create("dev") {
            dimension = "backend"
            buildConfigField("String", "BACKEND_URL", "\"\"")
        }

        create("prod") {
            dimension = "backend"
            buildConfigField("String", "BACKEND_URL", "\"\"")
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
        buildConfig = true
    }
    packaging {
        resources.excludes += setOf(
            "/META-INF/{AL2.0,LGPL2.1}",
            "/META-INF/INDEX.LIST",
            "/META-INF/io.netty.versions.properties"
        )
    }
}

dependencies {
    // Modules
    implementation(project(":core"))
    implementation(project(":features"))

    // UI
    implementation(libs.activity.compose)
    implementation(libs.splashscreen)

    // Architecture
    implementation(libs.bundles.decompose)
    implementation(libs.bundles.replica)
    implementation(libs.replica.core)

    // DI
    implementation(libs.koin)

    // Geo
    implementation(libs.yandex.mapkit)

    // DivKit
    implementation(libs.bundles.divkit.client)

    // Debugging
    debugImplementation(libs.bundles.hyperion)
    debugImplementation(libs.chucker)
    debugImplementation(libs.replica.devtools)
    implementation(libs.logger.kermit)
}
