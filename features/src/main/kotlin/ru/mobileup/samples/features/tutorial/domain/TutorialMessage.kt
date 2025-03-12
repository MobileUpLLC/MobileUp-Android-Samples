package ru.mobileup.samples.features.tutorial.domain

import dev.icerock.moko.resources.desc.StringDesc

data class TutorialMessage(
    val key: Any,
    val text: StringDesc,
    val onActionClick: () -> Unit
)

data class HighlightableItem(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
)