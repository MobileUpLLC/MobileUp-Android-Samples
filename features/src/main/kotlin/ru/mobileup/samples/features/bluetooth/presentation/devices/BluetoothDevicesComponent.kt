package ru.mobileup.samples.features.bluetooth.presentation.devices

import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.features.bluetooth.domain.BluetoothScannedDevices
import ru.mobileup.samples.features.bluetooth.domain.BluetoothServiceState
import ru.mobileup.samples.features.bluetooth.domain.DeviceAddress
import ru.mobileup.samples.features.bluetooth.domain.events.BluetoothScanningState

interface BluetoothDevicesComponent {
    val bluetoothState: StateFlow<BluetoothServiceState>
    val bluetoothScannedDevices: StateFlow<BluetoothScannedDevices>
    val scanningState: StateFlow<BluetoothScanningState>

    fun openBluetoothPermission()
    fun startScanning()
    fun onClickDevice(address: DeviceAddress)
}