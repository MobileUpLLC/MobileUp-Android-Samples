package ru.mobileup.samples.features.navigation.nested.leaf

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.localized
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme

@Composable
fun NavigationNestedLeafUi(
    component: NavigationNestedLeafComponent,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 32.dp)
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = component.name.localized(),
            style = CustomTheme.typography.title.regular,
            color = CustomTheme.colors.text.primary,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun NavigationNestedLeafUiPreview() {
    AppTheme {
        NavigationNestedLeafUi(FakeNavigationNestedLeafComponent())
    }
}