package ru.mobileup.samples.features.audio.presentation.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.localized
import ru.mobileup.samples.core.widget.button.AppButton
import ru.mobileup.samples.core.widget.button.ButtonType
import ru.mobileup.samples.features.audio.domain.AudioMenu

@Composable
fun AudioMenuUi(
    component: AudioMenuComponent,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp)
            .systemBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        AudioMenu.entries.forEach { menuItem ->
            AppButton(
                modifier = Modifier.fillMaxWidth(),
                buttonType = ButtonType.Secondary,
                text = menuItem.displayName.localized(),
                onClick = { component.onClickMenuItem(menuItem) }
            )
        }
    }
}