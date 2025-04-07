package ru.mobileup.samples.features.image.domain

import kotlinx.serialization.Serializable
import ru.mobileup.samples.core.media.ImageResource

@Serializable
data class ImageCarousel(
    val imageResources: List<ImageResource>,
    val currentImagePosition: Int
)