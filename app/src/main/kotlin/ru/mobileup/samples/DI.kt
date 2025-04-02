package ru.mobileup.samples

import ru.mobileup.samples.core.coreModule
import ru.mobileup.samples.features.document.documentModule
import ru.mobileup.samples.features.optimistic_update.optimisticUpdateModule
import ru.mobileup.samples.features.photo.photoModule
import ru.mobileup.samples.features.video.videoModule

val allModules = listOf(
    coreModule(BuildConfig.BACKEND_URL),
    photoModule,
    videoModule,
    photoModule,
    optimisticUpdateModule,
    documentModule
)