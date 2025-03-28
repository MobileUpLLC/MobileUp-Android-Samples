package ru.mobileup.samples

import ru.mobileup.samples.core.coreModule
import ru.mobileup.samples.features.pin_code.pinCodeModule
import ru.mobileup.samples.features.photo.photoModule
import ru.mobileup.samples.features.video.videoModule

val allModules = listOf(
    coreModule(BuildConfig.BACKEND_URL),
    videoModule,
    photoModule,
    pinCodeModule
)