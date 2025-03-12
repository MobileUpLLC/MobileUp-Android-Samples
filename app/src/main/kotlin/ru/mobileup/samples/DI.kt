package ru.mobileup.samples

import ru.mobileup.samples.core.coreModule
import ru.mobileup.samples.features.tutorial.tutorialModule
import ru.mobileup.samples.features.video.videoModule

val allModules = listOf(
    coreModule(BuildConfig.BACKEND_URL),
    videoModule,
    tutorialModule
)