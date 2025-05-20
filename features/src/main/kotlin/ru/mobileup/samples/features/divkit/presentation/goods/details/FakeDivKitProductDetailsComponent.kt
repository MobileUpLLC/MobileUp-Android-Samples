package ru.mobileup.samples.features.divkit.presentation.goods.details

import com.yandex.div.core.view2.Div2View
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeDivKitProductDetailsComponent : DivKitProductDetailsComponent {
    override val content: StateFlow<Div2View?> = MutableStateFlow(null)
    override val productId: Long = 0
}