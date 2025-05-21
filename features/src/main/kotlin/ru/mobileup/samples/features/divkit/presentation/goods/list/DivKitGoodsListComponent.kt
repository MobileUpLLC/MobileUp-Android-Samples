package ru.mobileup.samples.features.divkit.presentation.goods.list

import com.yandex.div.core.view2.Div2View
import kotlinx.coroutines.flow.StateFlow

interface DivKitGoodsListComponent {

    companion object {
        const val SORT_BY_NAME_TYPE = "Sort by name"
        const val SORT_BY_WEIGHT_TYPE = "Sort by weight"
        const val DETAILS_URL_PATH = "div_action://details"
        const val OPEN_DETAILS = "open_details"
        const val QUERY_ID = "id"
        const val APPLY_SORT = "apply_sort"
        const val SORT_NAME = "sort_name"
        fun createOpenDetailsUrl(id: Long): String = "$DETAILS_URL_PATH/$OPEN_DETAILS?$QUERY_ID=$id"
        fun createApplySortUrl(
            sortName: String
        ): String = "$DETAILS_URL_PATH/$APPLY_SORT?$SORT_NAME=$sortName"
    }

    val content: StateFlow<Div2View?>

    sealed interface Output {
        data class DetailsRequested(val id: Long) : Output
    }
}