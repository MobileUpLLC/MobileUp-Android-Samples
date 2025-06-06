package ru.mobileup.samples.features.bluetooth.presentation.devices

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.utils.SystemBars
import ru.mobileup.samples.features.bluetooth.domain.BluetoothServiceState
import ru.mobileup.samples.features.bluetooth.domain.events.BluetoothScanningState
import ru.mobileup.samples.features.bluetooth.presentation.widget.BluetoothDevicesList
import ru.mobileup.samples.features.bluetooth.presentation.widget.BluetoothStateUi

@Composable
fun BluetoothDevicesUi(
    component: BluetoothDevicesComponent,
    modifier: Modifier = Modifier
) {

    SystemBars(
        statusBarColor = Color.Transparent,
        navigationBarColor = Color.Transparent
    )

    val bluetoothState by component.bluetoothState.collectAsState()

    val bluetoothDevices by component.bluetoothScannedDevices.collectAsState()

    val scanningState by component.scanningState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BluetoothStateUi(
            state = bluetoothState,
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .statusBarsPadding()
                .padding(top = 16.dp)
        )

        if (bluetoothState is BluetoothServiceState.BluetoothOn) {
            BluetoothDevicesList(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                devices = bluetoothDevices,
                scanningState = scanningState,
                onClickScanning = component::startScanning,
                onClickDevice = component::onClickDevice,
                onOpenSettings = component::openBluetoothPermission
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewBluetoothDevicesUi() {
    AppTheme {
        BluetoothDevicesUi(
            FakeBluetoothDevicesComponent(
                BluetoothServiceState.BluetoothOn,
                BluetoothScanningState.NonPermission
            )
        )
    }
}