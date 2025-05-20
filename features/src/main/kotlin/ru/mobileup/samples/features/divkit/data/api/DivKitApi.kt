package ru.mobileup.samples.features.divkit.data.api

interface DivKitApi {
    suspend fun getGoodsListData(
        sortByNameEnable: Boolean,
        sortByWeightEnable: Boolean
    ): String
    suspend fun getProductById(id: Long): String
}