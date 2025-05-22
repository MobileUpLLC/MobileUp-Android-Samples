package ru.mobileup.samples.features.multipane_menu.presentation.list

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.panels.ChildPanelsMode
import dev.icerock.moko.resources.compose.localized
import ru.mobileup.samples.core.message.presentation.noOverlapByMessage
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.widget.button.AppButton
import ru.mobileup.samples.core.widget.button.ButtonType
import ru.mobileup.samples.features.menu.domain.Sample

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun SampleListUi(
    component: SampleListComponent,
    mode: ChildPanelsMode,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            if (mode == ChildPanelsMode.SINGLE) {
                FloatingActionButton(
                    modifier = Modifier.noOverlapByMessage(),
                    onClick = component::onSettingsClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = CustomTheme.colors.icon.primary
                    )
                }
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

            /* Manually add bottom spacing equal to FAB height because the padding
             provided by Scaffold is not always correct on all Android versions. */
            if (mode == ChildPanelsMode.SINGLE) Spacer(Modifier.height(56.dp))
        }
    }
}
