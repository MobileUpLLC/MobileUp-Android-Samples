package ru.mobileup.samples.features.image.domain

import kotlinx.serialization.Serializable

@Serializable
data class ImageCarousel(
    val imageResources: List<ImageResource>,
    val currentImagePosition: Int
)