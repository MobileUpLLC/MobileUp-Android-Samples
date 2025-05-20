package ru.mobileup.samples.features.divkit.data.db

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.mobileup.samples.features.divkit.domain.DivKitProductType
import kotlin.random.Random

class DivKitGoodsDbImpl : DivKitGoodsDb {
    override suspend fun getGoods(
        sortByNameEnable: Boolean,
        sortByWeightEnable: Boolean
    ): List<DivKitProductDBO> = withContext(Dispatchers.IO) {
        val list = productsList +
            productsList.map {
                it.copy(
                    name = "${it.name} sweet",
                    weight = it.weight + Random.nextInt(10_000)
                )
            } + productsList.map {
                it.copy(
                    name = "${it.name} sour",
                    weight = (it.weight - Random.nextInt(10_000)).coerceAtLeast(0)
                )
            }
        return@withContext when {
            sortByNameEnable -> list.sortedBy { it.name }
            sortByWeightEnable -> list.sortedBy { it.weight }
            else -> list.shuffled()
        }
    }

    override suspend fun getProductById(id: Long): DivKitProductDBO {
        return withContext(Dispatchers.IO) {
            productsList.firstOrNull { it.id == id } ?: productsList.first()
        }
    }
}

private val productsList = listOf(
    DivKitProductDBO(
        id = 0,
        name = "Banana",
        type = DivKitProductType.Fruit.name,
        imageUrl = "divkit-asset://banana.webp"
    ),
    DivKitProductDBO(
        id = 1,
        name = "Potato",
        type = DivKitProductType.Vegetable.name,
        imageUrl = "divkit-asset://potato.webp"
    ),
    DivKitProductDBO(
        id = 2,
        name = "Carrot",
        type = DivKitProductType.Vegetable.name,
        imageUrl = "divkit-asset://carrot.webp"
    ),
    DivKitProductDBO(
        id = 3,
        name = "Apple",
        type = DivKitProductType.Fruit.name,
        imageUrl = "divkit-asset://apple.webp"
    ),
    DivKitProductDBO(
        id = 4,
        name = "Cherry",
        type = DivKitProductType.Fruit.name,
        imageUrl = "divkit-asset://cherry.webp"
    ),
    DivKitProductDBO(
        id = 5,
        name = "Grape",
        type = DivKitProductType.Fruit.name,
        imageUrl = "divkit-asset://grape.webp"
    ),
    DivKitProductDBO(
        id = 6,
        name = "Mango",
        type = DivKitProductType.Fruit.name,
        imageUrl = "divkit-asset://mango.webp"
    ),
    DivKitProductDBO(
        id = 7,
        name = "Watermelon",
        type = DivKitProductType.Fruit.name,
        imageUrl = "divkit-asset://watermelon.webp"
    ),
    DivKitProductDBO(
        id = 8,
        name = "Pear",
        type = DivKitProductType.Fruit.name,
        imageUrl = "divkit-asset://pear.webp"
    ),
    DivKitProductDBO(
        id = 9,
        name = "Lime",
        type = DivKitProductType.Fruit.name,
        imageUrl = "divkit-asset://lime.webp"
    ),
    DivKitProductDBO(
        id = 10,
        name = "Peas",
        type = DivKitProductType.Vegetable.name,
        imageUrl = "divkit-asset://peas.webp"
    ),
    DivKitProductDBO(
        id = 11,
        name = "Beet",
        type = DivKitProductType.Vegetable.name,
        imageUrl = "divkit-asset://beet.webp"
    ),
    DivKitProductDBO(
        id = 12,
        name = "Pepper",
        type = DivKitProductType.Vegetable.name,
        imageUrl = "divkit-asset://pepper.webp"
    ),
    DivKitProductDBO(
        id = 13,
        name = "Radish",
        type = DivKitProductType.Vegetable.name,
        imageUrl = "divkit-asset://radish.webp"
    ),
    DivKitProductDBO(
        id = 14,
        name = "Cucumber",
        type = DivKitProductType.Vegetable.name,
        imageUrl = "divkit-asset://cucumber.webp"
    ),
    DivKitProductDBO(
        id = 15,
        name = "Bean",
        type = DivKitProductType.Vegetable.name,
        imageUrl = "divkit-asset://bean.webp"
    )
)