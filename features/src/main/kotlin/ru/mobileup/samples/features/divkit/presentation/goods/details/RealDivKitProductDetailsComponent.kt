package ru.mobileup.samples.features.divkit.presentation.goods.details

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

class RealDivKitProductDetailsComponent(
    componentContext: ComponentContext,
    override val productId: Long,
    private val divKitContext: Div2Context,
    divKitRepository: DivKitRepository,
    errorHandler: ErrorHandler,
    private val onBackClick: () -> Unit,
) : ComponentContext by componentContext, DivKitProductDetailsComponent {
    override val content = MutableStateFlow<Div2View?>(null)

    init {
        componentScope.safeLaunch(errorHandler) {
            val divKitData = divKitRepository.getProductByIdData(productId)
            content.update {
                Div2View(divKitContext).apply {
                    setData(divKitData, divKitData.tag)
                    actionHandler = createActionHandler()
                }
            }
        }
    }

    private fun createActionHandler() = object : DivActionHandler() {
        override fun handleAction(
            action: DivAction,
            view: DivViewFacade,
            resolver: ExpressionResolver
        ): Boolean {
            val url = action.url?.evaluate(resolver).toString()
            if (url.contains("back")) onBackClick()
            return super.handleAction(action, view, resolver)
        }
    }
}