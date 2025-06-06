package ru.mobileup.samples.features.bluetooth.presentation.widget

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.widget.button.AppButton
import ru.mobileup.samples.core.widget.button.ButtonType
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.bluetooth.domain.BluetoothFoundDevice
import ru.mobileup.samples.features.bluetooth.domain.BluetoothScannedDevices
import ru.mobileup.samples.features.bluetooth.domain.DeviceAddress
import ru.mobileup.samples.features.bluetooth.domain.events.BluetoothScanningState
import ru.mobileup.samples.features.bluetooth.domain.events.inError
import ru.mobileup.samples.features.bluetooth.domain.events.inProgress
import ru.mobileup.samples.features.bluetooth.domain.events.nonPermissions

@Composable
fun BluetoothDevicesList(
    devices: BluetoothScannedDevices,
    scanningState: BluetoothScanningState,
    onOpenSettings: () -> Unit,
    onClickDevice: (DeviceAddress) -> Unit,
    onClickScanning: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bottomPadding = WindowInsets.navigationBars.asPaddingValues()

    LazyColumn(
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(CustomTheme.colors.chat.input)
            .then(modifier),
        contentPadding = PaddingValues(
            top = 16.dp,
            start = 16.dp,
            end = 16.dp,
            bottom = 16.dp + bottomPadding.calculateBottomPadding()
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        if (devices.pairedDevices.isNotEmpty()) {
            devicesLabel(
                key = "PairedDevicesLabel",
                text = { stringResource(R.string.bluetooth_paired_devices) }
            )
            devicesItems(devices.pairedDevices)
        }

        devicesLabel(
            key = "ScanningDevicesLabel",
            text = { stringResource(R.string.bluetooth_scanning_devices) },
            textEndContent = {
                val infiniteTransition = rememberInfiniteTransition()
                val animatedRotate = infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = if (scanningState.inProgress()) 360f else 0f,
                    animationSpec = InfiniteRepeatableSpec(
                        animation = tween(durationMillis = 1000),
                        repeatMode = RepeatMode.Restart
                    )
                )

                IconButton(
                    onClick = onClickScanning,
                    enabled = !scanningState.inProgress()
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_rotate),
                        contentDescription = "refresh_scanning_devices",
                        modifier = Modifier
                            .graphicsLayer { rotationZ = animatedRotate.value }
                    )
                }
            }
        )

        if (scanningState.inError()) {
            scanningError()
        } else if (scanningState.nonPermissions()) {
            permissionError(onOpenSettings)
        } else {
            devicesItems(devices.scanningDevices, onClick = onClickDevice)
        }
    }
}

private fun LazyListScope.devicesLabel(
    key: String,
    text: @Composable () -> String,
    textEndContent: @Composable () -> Unit = {}
) {
    item(key = key) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text(),
                style = CustomTheme.typography.body.regular.copy(fontWeight = FontWeight.Bold),
                color = CustomTheme.colors.text.primary,
                modifier = Modifier.weight(1f)
            )
            textEndContent()
        }
    }
}

private fun LazyListScope.devicesItems(
    items: List<BluetoothFoundDevice>,
    onClick: ((DeviceAddress) -> Unit)? = null
) {
    items(
        items,
        key = { it.address.value }
    ) { device ->
        BluetoothDeviceUi(
            bluetoothFoundDevice = device,
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private fun LazyListScope.scanningError() {
    item(key = "ScanningError") {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(CustomTheme.colors.common.negative.copy(alpha = 0.3f))
                .padding(16.dp),
        ) {
            Text(
                text = stringResource(R.string.bluetooth_scanning_error),
                style = CustomTheme.typography.title.regular,
                color = CustomTheme.colors.text.primary,
            )
        }
    }
}

private fun LazyListScope.permissionError(onOpenSettings: () -> Unit) {
    item(key = "PermissionError") {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(CustomTheme.colors.common.negative.copy(alpha = 0.3f))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.bluetooth_scanning_non_permissions),
                style = CustomTheme.typography.body.regular,
                color = CustomTheme.colors.text.primary,
            )

            AppButton(
                modifier = Modifier.align(Alignment.End),
                buttonType = ButtonType.Secondary,
                onClick = onOpenSettings
            ) {
                Text(
                    text = stringResource(R.string.bluetooth_open_settings),
                    style = CustomTheme.typography.caption.regular,
                    color = CustomTheme.colors.text.primary,
                )
            }
        }
    }
}