package ru.mobileup.samples.features.menu.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.localized
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.widget.button.AppButton
import ru.mobileup.samples.core.widget.button.ButtonType
import ru.mobileup.samples.features.menu.domain.Sample

@Composable
fun MenuUi(
    component: MenuComponent,
    modifier: Modifier = Modifier,
) {
    // Measure FAB height manually because the padding provided by Scaffold
    // is not always correct on all Android versions.
    var fabHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.onSizeChanged {
                    fabHeight = density.run { it.height.toDp() }
                },
                onClick = component::onSettingsClick
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = CustomTheme.colors.icon.primary
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .systemBarsPadding()
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Sample.entries.forEach { sample ->
                AppButton(
                    modifier = Modifier.fillMaxWidth(),
                    buttonType = ButtonType.Secondary,
                    text = sample.displayName.localized(),
                    onClick = { component.onButtonClick(sample) }
                )
            }
            Spacer(Modifier.height(fabHeight))
        }
    }
}

@Preview
@Composable
private fun MenuUiPreview() {
    AppTheme {
        MenuUi(FakeMenuComponent())
    }
}
