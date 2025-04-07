package ru.mobileup.samples.features.shared_element_transitions.domain

import kotlinx.serialization.Serializable
import ru.mobileup.samples.core.media.ImageResource

@Serializable
data class ItemSharedElement(
    val id: Int,
    val title: String,
    val text: String,
    val image: ImageResource
)