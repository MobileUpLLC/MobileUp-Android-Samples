plugins {
    alias(libs.plugins.convetion.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktorfit)
    alias(libs.plugins.module.graph)
}

android {
    namespace = "ru.mobileup.samples.features"
}

dependencies {
    ksp(libs.ktorfit.ksp)

    // Modules
    implementation(project(":core"))

    // Kotlin
    implementation(libs.kotlinx.datetime)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // UI
    implementation(libs.bundles.compose)
    implementation(libs.compose.material.icons)
    // TODO( Remove when a stable release is available)
    implementation("androidx.compose.material3:material3:1.4.0-alpha10")
    implementation(libs.accompanist.swiperefresh)
    implementation(libs.bundles.coil)

    // DI
    implementation(libs.koin)

    // Logging
    implementation(libs.logger.kermit)

    // Network
    implementation(libs.bundles.ktor)
    implementation(libs.ktorfit.lib)

    implementation(libs.form.validation)

    // Architecture
    implementation(libs.bundles.decompose)
    implementation(libs.bundles.replica)
    api(libs.moko.resources)
    implementation(libs.moko.resourcesCompose)

    // Camera
    implementation(libs.camera.core)
    implementation(libs.camera.camera2)
    implementation(libs.camera.lifecycle)
    implementation(libs.camera.video)
    implementation(libs.camera.view)

    // Player
    implementation(libs.media3.exoplayer)

    // Transformer
    implementation(libs.media3.transformer)

    // Calendar
    implementation(libs.compose.calendar)

    // Qr code generation
    implementation(libs.qrose)

    // Qr code scanning
    implementation(libs.barcode.scanning)

    // Charts
    implementation(libs.vico.compose)

    // Google service
    implementation(libs.google.playservice.auth)
    implementation(libs.google.playservice.auth.phone)

    // Geo
    implementation(libs.yandex.mapkit)

    // Image Cropping
    implementation(libs.android.image.cropper)
}

// Usage: ./gradlew generateModuleGraph detectGraphCycles
moduleGraph {
    featuresPackage.set("ru.mobileup.samples.features")
    featuresDirectory.set(project.file("src/main/kotlin/ru/mobileup/samples/features"))
    outputDirectory.set(project.file("module_graph"))
}
