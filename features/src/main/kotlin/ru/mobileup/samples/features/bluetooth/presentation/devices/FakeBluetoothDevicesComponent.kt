package ru.mobileup.samples.features.bluetooth.presentation.devices

import kotlinx.coroutines.flow.MutableStateFlow
import ru.mobileup.samples.features.bluetooth.domain.BluetoothScannedDevices
import ru.mobileup.samples.features.bluetooth.domain.BluetoothServiceState
import ru.mobileup.samples.features.bluetooth.domain.DeviceAddress
import ru.mobileup.samples.features.bluetooth.domain.events.BluetoothScanningState

class FakeBluetoothDevicesComponent(
    bluetoothState: BluetoothServiceState,
    scanningState: BluetoothScanningState
) : BluetoothDevicesComponent {

    override val bluetoothState = MutableStateFlow(bluetoothState)

    override val bluetoothScannedDevices = MutableStateFlow(BluetoothScannedDevices.MOCK)
    override val scanningState = MutableStateFlow(scanningState)

    override fun openBluetoothPermission() = Unit
    override fun startScanning() = Unit
    override fun onClickDevice(address: DeviceAddress) = Unit
}