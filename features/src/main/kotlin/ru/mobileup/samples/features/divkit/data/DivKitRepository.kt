package ru.mobileup.samples.features.divkit.data

import com.yandex.div2.DivData

interface DivKitRepository {
    suspend fun getMenuDataData(): DivData
    suspend fun getExampleData(jsonName: String): DivData
    suspend fun getGoodsListData(
        sortByNameEnable: Boolean,
        sortByWeightEnable: Boolean
    ): DivData
    suspend fun getProductByIdData(id: Long): DivData
}