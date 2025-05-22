package ru.mobileup.samples.features.divkit.presentation.goods.list

import androidx.core.net.toUri
import com.arkivanov.decompose.ComponentContext
import com.yandex.div.core.Div2Context
import com.yandex.div.core.DivActionHandler
import com.yandex.div.core.DivViewFacade
import com.yandex.div.core.view2.Div2View
import com.yandex.div.json.expressions.ExpressionResolver
import com.yandex.div2.DivAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import ru.mobileup.samples.core.error_handling.ErrorHandler
import ru.mobileup.samples.core.error_handling.safeLaunch
import ru.mobileup.samples.core.utils.componentScope
import ru.mobileup.samples.features.divkit.data.DivKitRepository
import ru.mobileup.samples.features.divkit.presentation.extension.tag

class RealDivKitGoodsListComponent(
    componentContext: ComponentContext,
    private val onOutput: (DivKitGoodsListComponent.Output) -> Unit,
    private val divKitContext: Div2Context,
    private val divKitRepository: DivKitRepository,
    private val errorHandler: ErrorHandler
) : ComponentContext by componentContext, DivKitGoodsListComponent {

    override val content = MutableStateFlow<Div2View?>(null)

    private var sortByNameEnable = false
    private var sortByWeightEnable = false

    init {
        fetchData()
    }

    private fun createActionHandler() = object : DivActionHandler() {
        override fun handleAction(
            action: DivAction,
            view: DivViewFacade,
            resolver: ExpressionResolver
        ): Boolean {
            val url = action.url?.evaluate(resolver).toString()
            if (url.contains(DivKitGoodsListComponent.DETAILS_URL_PATH)) {
                when {
                    url.contains(DivKitGoodsListComponent.OPEN_DETAILS) -> {
                        val contentId = url.toUri().getQueryParameter(DivKitGoodsListComponent.QUERY_ID)?.toLong()
                        contentId?.let { id ->
                            onOutput(DivKitGoodsListComponent.Output.DetailsRequested(id))
                        }
                    }
                    url.contains(DivKitGoodsListComponent.APPLY_SORT) -> {
                        val sortString = url.toUri().getQueryParameter(DivKitGoodsListComponent.SORT_NAME)
                        when (sortString) {
                            DivKitGoodsListComponent.SORT_BY_NAME_TYPE -> {
                                sortByNameEnable = !sortByNameEnable
                                sortByWeightEnable = false
                                fetchData()
                            }
                            DivKitGoodsListComponent.SORT_BY_WEIGHT_TYPE -> {
                                sortByNameEnable = false
                                sortByWeightEnable = !sortByWeightEnable
                                fetchData()
                            }
                        }
                    }
                }
            }
            return super.handleAction(action, view, resolver)
        }
    }

    private fun fetchData() {
        componentScope.safeLaunch(errorHandler) {
            content.value = null
            val divKitData = divKitRepository.getGoodsListData(sortByNameEnable, sortByWeightEnable)
            content.update {
                Div2View(divKitContext).apply {
                    setData(divKitData, divKitData.tag)
                    actionHandler = createActionHandler()
                }
            }
        }
    }
}