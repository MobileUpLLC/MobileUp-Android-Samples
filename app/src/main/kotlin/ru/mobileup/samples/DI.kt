package ru.mobileup.samples

import ru.mobileup.samples.core.coreModule
import ru.mobileup.samples.features.video.videoModule
import ru.mobileup.samples.features.yandex_map.yandexMapModule

val allModules = listOf(
    coreModule(BuildConfig.BACKEND_URL),
    videoModule,
    yandexMapModule
)