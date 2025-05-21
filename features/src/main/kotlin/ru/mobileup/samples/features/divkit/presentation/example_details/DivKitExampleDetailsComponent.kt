package ru.mobileup.samples.features.divkit.presentation.example_details

import com.yandex.div.core.view2.Div2View
import kotlinx.coroutines.flow.StateFlow

interface DivKitExampleDetailsComponent {

    val content: StateFlow<Div2View?>
    val title: String
    val jsonName: String
}