package ru.mobileup.samples.features.divkit.presentation.goods.details

import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import ru.mobileup.samples.features.divkit.presentation.widgets.DivKitView

@Composable
fun DivKitProductDetailsUi(
    component: DivKitProductDetailsComponent,
    modifier: Modifier = Modifier
) {
    val content by component.content.collectAsState()

    DivKitView(content, modifier.safeDrawingPadding())
}