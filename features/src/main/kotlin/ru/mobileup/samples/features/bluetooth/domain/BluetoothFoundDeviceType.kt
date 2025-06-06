package ru.mobileup.samples.features.bluetooth.domain

sealed interface BluetoothFoundDeviceType {
    data object Computer : BluetoothFoundDeviceType
    data object Phone : BluetoothFoundDeviceType
    data object AudioVideoOutput : BluetoothFoundDeviceType
    data object Unknown : BluetoothFoundDeviceType
}
