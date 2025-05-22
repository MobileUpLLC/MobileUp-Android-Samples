package ru.mobileup.samples.features.divkit.data.db

interface DivKitGoodsDb {
    suspend fun getGoods(
        sortByNameEnable: Boolean,
        sortByWeightEnable: Boolean
    ): List<DivKitProductDBO>
    suspend fun getProductById(id: Long): DivKitProductDBO
}