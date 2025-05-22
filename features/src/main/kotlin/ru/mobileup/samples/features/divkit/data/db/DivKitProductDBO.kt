package ru.mobileup.samples.features.divkit.data.db

import ru.mobileup.samples.features.divkit.domain.DivKitProduct
import ru.mobileup.samples.features.divkit.domain.DivKitProductType
import kotlin.random.Random

data class DivKitProductDBO(
    val id: Long,
    val weight: Int = Random.nextInt(10_000),
    val name: String,
    val type: String,
    val imageUrl: String
) {
    fun toDomain() = DivKitProduct(
        id = id,
        weight = weight,
        name = name,
        type = if (type == DivKitProductType.Fruit.name) DivKitProductType.Fruit else DivKitProductType.Vegetable,
        imageUrl = imageUrl
    )
}
