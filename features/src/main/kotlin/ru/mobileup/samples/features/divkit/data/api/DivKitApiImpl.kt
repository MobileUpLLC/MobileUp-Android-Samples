package ru.mobileup.samples.features.divkit.data.api

import com.fasterxml.jackson.databind.json.JsonMapper
import divkit.dsl.State
import divkit.dsl.action
import divkit.dsl.bold
import divkit.dsl.center
import divkit.dsl.color
import divkit.dsl.container
import divkit.dsl.core.bind
import divkit.dsl.data
import divkit.dsl.divan
import divkit.dsl.edgeInsets
import divkit.dsl.end
import divkit.dsl.fixedSize
import divkit.dsl.horizontal
import divkit.dsl.image
import divkit.dsl.matchParentSize
import divkit.dsl.overlap
import divkit.dsl.render
import divkit.dsl.scope.DivScope
import divkit.dsl.solidBackground
import divkit.dsl.state
import divkit.dsl.stateItem
import divkit.dsl.text
import divkit.dsl.url
import divkit.dsl.vertical
import kotlinx.coroutines.delay
import ru.mobileup.samples.features.divkit.data.DivKitTemplate
import ru.mobileup.samples.features.divkit.data.db.DivKitGoodsDb
import ru.mobileup.samples.features.divkit.data.db.DivKitProductDBO
import ru.mobileup.samples.features.divkit.domain.DivKitProduct
import ru.mobileup.samples.features.divkit.domain.DivKitProductType
import ru.mobileup.samples.features.divkit.presentation.goods.list.DivKitGoodsListComponent

class DivKitApiImpl(
    private val db: DivKitGoodsDb,
    private val jsonMapper: JsonMapper
) : DivKitApi {

    override suspend fun getGoodsListData(
        sortByNameEnable: Boolean,
        sortByWeightEnable: Boolean
    ): String {
        delay(1000)
        val goods = db.getGoods(
            sortByNameEnable = sortByNameEnable,
            sortByWeightEnable = sortByWeightEnable
        ).map(DivKitProductDBO::toDomain)
        val categoriesList = listOf(
            "All",
            DivKitProductType.Fruit.name,
            DivKitProductType.Vegetable.name
        )
        val filters = listOf(
            "Sort by name" to sortByNameEnable,
            "Sort by weight" to sortByWeightEnable
        )

        val card = divan {
            data(
                logId = "goods_list_data",
                div = state(
                    id = "goods_list_state",
                    states = categoriesList.map { category ->
                        item(
                            currentCategory = category,
                            categories = categoriesList,
                            goods = goods,
                            filters = filters
                        )
                    }
                )
            )
        }

        return jsonMapper.writeValueAsString(card)
    }

    override suspend fun getProductById(id: Long): String {
        val product = db.getProductById(id)

        val card = divan {
            data(
                logId = "product_details_$id",
                div = container(
                    items = listOf(
                        container(
                            orientation = overlap,
                            contentAlignmentVertical = center,
                            items = listOf(
                                image(
                                    imageUrl = url("divkit-asset://arrow_back.webp"),
                                    width = fixedSize(32),
                                    height = fixedSize(32),
                                    margins = edgeInsets(16),
                                    action = action(
                                        logId = "product_details_back_button_click",
                                        url = url("div-action://back")
                                    )
                                ),
                                text(
                                    text = product.name,
                                    textAlignmentHorizontal = center,
                                    fontSize = 36,
                                    fontWeight = bold,
                                    margins = edgeInsets(16)
                                )
                            )
                        ),
                        container(
                            orientation = horizontal,
                            margins = edgeInsets(8),
                            paddings = edgeInsets(8),
                            items = listOf(
                                text(
                                    text = "Balance",
                                    fontSize = 24
                                ),
                                text(
                                    text = "${product.weight}g",
                                    fontSize = 24,
                                    textAlignmentHorizontal = end
                                )
                            )
                        ),
                        image(
                            imageUrl = url(product.imageUrl),
                            width = matchParentSize()
                        )
                    )
                )
            )
        }

        return jsonMapper.writeValueAsString(card)
    }
}

private fun DivScope.item(
    currentCategory: String,
    categories: List<String>,
    goods: List<DivKitProduct>,
    filters: List<Pair<String, Boolean>>
): State.Item {
    val filteredGoods = when (currentCategory) {
        DivKitProductType.Fruit.name -> goods.filter { it.type == DivKitProductType.Fruit }
        DivKitProductType.Vegetable.name -> goods.filter { it.type == DivKitProductType.Vegetable }
        else -> goods
    }
    return stateItem(
        stateId = currentCategory,
        div = container(
            items = listOf(
                container(
                    orientation = horizontal,
                    margins = edgeInsets(16),
                    items = filters.map { (filterName, enabled) ->
                        with(DivKitTemplate.CategoryTitle) {
                            render(
                                template,
                                titleRef bind filterName,
                                backgroundColorRef bind listOf(
                                    solidBackground(
                                        color = color(if (enabled) "#3F54DF" else "#FFFFFFFF")
                                    )
                                ),
                                textColorRef bind color(if (enabled) "#FFFFFFFF" else "#000000"),
                                clickActionRef bind action(
                                    logId = "on_filter_category_click_$filterName",
                                    url = url(DivKitGoodsListComponent.createApplySortUrl(filterName))
                                ),
                            )
                        }
                    }
                ),
                container(
                    orientation = horizontal,
                    margins = edgeInsets(16),
                    items = categories.map { category ->
                        with(DivKitTemplate.CategoryTitle) {
                            render(
                                template,
                                titleRef bind category,
                                backgroundColorRef bind listOf(
                                    solidBackground(
                                        color = color(if (currentCategory == category) "#3F54DF" else "#FFFFFFFF")
                                    )
                                ),
                                textColorRef bind color(if (currentCategory == category) "#FFFFFFFF" else "#000000"),
                                clickActionRef bind action(
                                    logId = "on_product_category_click_$category",
                                    url = url("div-action://set_state?state_id=0/goods_list_state/$category")
                                ),
                            )
                        }
                    }
                ),
                container(
                    orientation = vertical,
                    items = filteredGoods.map { product ->
                        with(DivKitTemplate.Product) {
                            render(
                                template,
                                titleRef bind product.name,
                                weightRef bind "${product.weight}g",
                                clickActionRef bind action(
                                    logId = "on_product_click_${product.id}",
                                    url = url(DivKitGoodsListComponent.createOpenDetailsUrl(product.id))
                                ),
                                iconUrl bind url(product.imageUrl)
                            )
                        }
                    }
                )
            )
        )
    )
}
