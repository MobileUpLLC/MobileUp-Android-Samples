package ru.mobileup.samples.features.divkit.domain

data class DivKitProduct(
    val id: Long,
    val weight: Int,
    val name: String,
    val type: DivKitProductType,
    val imageUrl: String
)
