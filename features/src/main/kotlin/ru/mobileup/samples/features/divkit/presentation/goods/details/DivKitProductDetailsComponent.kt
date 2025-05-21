package ru.mobileup.samples.features.divkit.presentation.goods.details

import com.yandex.div.core.view2.Div2View
import kotlinx.coroutines.flow.StateFlow

interface DivKitProductDetailsComponent {
    val content: StateFlow<Div2View?>
    val productId: Long
}