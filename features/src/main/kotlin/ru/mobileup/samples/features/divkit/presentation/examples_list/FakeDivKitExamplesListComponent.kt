package ru.mobileup.samples.features.divkit.presentation.examples_list

import com.yandex.div.core.view2.Div2View
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeDivKitExamplesListComponent : DivKitExamplesListComponent {

    override val content: StateFlow<Div2View?> = MutableStateFlow(null)
}