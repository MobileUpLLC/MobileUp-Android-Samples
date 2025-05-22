package ru.mobileup.samples.features.divkit.presentation.goods.list

import com.yandex.div.core.view2.Div2View
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeDivKitGoodsListComponent : DivKitGoodsListComponent {
    override val content: StateFlow<Div2View?> = MutableStateFlow(null)
}