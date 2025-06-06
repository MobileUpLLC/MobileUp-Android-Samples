package ru.mobileup.samples.features.bluetooth.domain

import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.strResDesc
import ru.mobileup.samples.core.domain.DisplayedEnum
import ru.mobileup.samples.features.R

enum class BluetoothMenu(
    override val displayName: StringDesc
) : DisplayedEnum {
    Devices(R.string.bluetooth_menu_devices.strResDesc())
}