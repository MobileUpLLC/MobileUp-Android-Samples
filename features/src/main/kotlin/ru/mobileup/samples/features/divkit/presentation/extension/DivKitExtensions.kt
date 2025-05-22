package ru.mobileup.samples.features.divkit.presentation.extension

import com.yandex.div.DivDataTag
import com.yandex.div2.DivData

val DivData.tag get() = DivDataTag(logId)