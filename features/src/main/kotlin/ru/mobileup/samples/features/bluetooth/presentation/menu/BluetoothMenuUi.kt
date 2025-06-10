package ru.mobileup.samples.features.bluetooth.presentation.menu

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
import ru.mobileup.samples.features.bluetooth.domain.BluetoothMenu

@Composable
fun BluetoothMenuUi(
    component: BluetoothMenuComponent,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp)
            .systemBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BluetoothMenu.entries.forEach {
            AppButton(
                modifier = Modifier.fillMaxWidth(),
                buttonType = ButtonType.Secondary,
                text = it.displayName.localized(),
                onClick = { component.onMenuSelect(it) }
            )
        }
    }
}