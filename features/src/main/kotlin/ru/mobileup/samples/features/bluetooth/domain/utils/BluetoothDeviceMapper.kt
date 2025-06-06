package ru.mobileup.samples.features.bluetooth.domain.utils

import android.Manifest
import android.bluetooth.BluetoothClass.Device.Major.AUDIO_VIDEO
import android.bluetooth.BluetoothClass.Device.Major.COMPUTER
import android.bluetooth.BluetoothClass.Device.Major.PHONE
import android.bluetooth.BluetoothDevice
import androidx.annotation.RequiresPermission
import ru.mobileup.samples.features.bluetooth.domain.BluetoothFoundDevice
import ru.mobileup.samples.features.bluetooth.domain.BluetoothFoundDeviceType
import ru.mobileup.samples.features.bluetooth.domain.DeviceAddress

@RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
fun BluetoothDevice.mapToFoundDevice(): BluetoothFoundDevice {
    return BluetoothFoundDevice(
        address = DeviceAddress(address),
        name = name,
        type = when (bluetoothClass.majorDeviceClass) {
            COMPUTER -> BluetoothFoundDeviceType.Computer
            PHONE -> BluetoothFoundDeviceType.Phone
            AUDIO_VIDEO -> BluetoothFoundDeviceType.AudioVideoOutput
            else -> BluetoothFoundDeviceType.Unknown
        },
        isPairingProcess = this.bondState == BluetoothDevice.BOND_BONDING
    )
}