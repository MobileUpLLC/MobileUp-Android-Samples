package ru.mobileup.samples.features.bluetooth.domain

import java.util.UUID

@JvmInline
value class DeviceAddress(val value: String)

data class BluetoothFoundDevice(
    val address: DeviceAddress,
    val name: String?,
    val type: BluetoothFoundDeviceType,
    val isPairingProcess: Boolean
)

data class BluetoothScannedDevices(
    val pairedDevices: List<BluetoothFoundDevice>,
    val scanningDevices: List<BluetoothFoundDevice>
) {
    companion object {
        val empty = BluetoothScannedDevices(
            pairedDevices = emptyList(),
            scanningDevices = emptyList()
        )
        val MOCK = BluetoothScannedDevices(
            pairedDevices = buildList {
                repeat(3) {
                    add(
                        BluetoothFoundDevice(
                            address = DeviceAddress(UUID.randomUUID().toString()),
                            name = "device: $it",
                            type = BluetoothFoundDeviceType.Phone,
                            isPairingProcess = false
                        )
                    )
                }
            },
            scanningDevices = buildList {
                repeat(6) {
                    add(
                        BluetoothFoundDevice(
                            address = DeviceAddress(UUID.randomUUID().toString()),
                            name = "device: $it",
                            type = BluetoothFoundDeviceType.Unknown,
                            isPairingProcess = true
                        )
                    )
                }
            }
        )
    }
}