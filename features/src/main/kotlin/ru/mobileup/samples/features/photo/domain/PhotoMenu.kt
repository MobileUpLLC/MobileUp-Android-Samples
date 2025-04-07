package ru.mobileup.samples.features.photo.domain

import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.strResDesc
import ru.mobileup.samples.core.domain.DisplayedEnum
import ru.mobileup.samples.features.R

enum class PhotoMenu(override val displayName: StringDesc) : DisplayedEnum {
    Camera(R.string.menu_item_camera.strResDesc()),
    Cropping(R.string.menu_item_cropping.strResDesc())
}