package ru.mobileup.samples.features.divkit.data

import android.content.Context
import com.yandex.div.data.DivParsingEnvironment
import com.yandex.div2.DivData
import kotlinx.coroutines.delay
import ru.mobileup.samples.features.divkit.data.api.DivKitApi
import ru.mobileup.samples.features.divkit.domain.AppDivData

class DivKitRepositoryImpl(
    private val context: Context,
    private val parser: DivParsingEnvironment,
    private val api: DivKitApi
) : DivKitRepository {

    companion object {
        private const val DELAY = 1000L
    }

    override suspend fun getMenuDataData() = delayed {
        getUiData("menu").toDivData(parser)
    }

    override suspend fun getExampleData(jsonName: String) = delayed {
        getUiData(jsonName).toDivData(parser)
    }

    override suspend fun getGoodsListData(
        sortByNameEnable: Boolean,
        sortByWeightEnable: Boolean
    ) = delayed {
        val jsonStr = api.getGoodsListData(sortByNameEnable, sortByWeightEnable)
        AppDivData.fromString(jsonStr).toDivData(parser)
    }

    override suspend fun getProductByIdData(id: Long) = delayed {
        val jsonStr = api.getProductById(id)
        AppDivData.fromString(jsonStr).toDivData(parser)
    }

    private fun getUiData(jsonName: String) = context.assets.open("divkit/$jsonName.json")
        .bufferedReader()
        .use { it.readText() }
        .let(AppDivData::fromString)

    private suspend fun delayed(action: suspend () -> DivData): DivData {
        delay(DELAY)
        return action()
    }
}