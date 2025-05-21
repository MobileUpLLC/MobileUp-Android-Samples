package ru.mobileup.samples.features.divkit.presentation.example_details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.features.divkit.presentation.widgets.DivKitView

@Composable
fun DivKitExampleDetailsUi(
    component: DivKitExampleDetailsComponent,
    modifier: Modifier = Modifier
) {
    val content by component.content.collectAsState()

    Column(
        modifier = modifier
            .safeDrawingPadding()
    ) {
        Text(
            text = component.title,
            style = CustomTheme.typography.title.regular.copy(fontWeight = FontWeight.ExtraBold),
            color = CustomTheme.colors.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        DivKitView(content)
    }
}