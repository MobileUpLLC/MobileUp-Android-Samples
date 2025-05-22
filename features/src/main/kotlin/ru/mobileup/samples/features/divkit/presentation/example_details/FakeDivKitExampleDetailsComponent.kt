package ru.mobileup.samples.features.divkit.presentation.example_details

import com.yandex.div.core.view2.Div2View
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeDivKitExampleDetailsComponent : DivKitExampleDetailsComponent {
    override val content: StateFlow<Div2View?> = MutableStateFlow(null)
    override val jsonName: String = ""
    override val title: String = ""
}