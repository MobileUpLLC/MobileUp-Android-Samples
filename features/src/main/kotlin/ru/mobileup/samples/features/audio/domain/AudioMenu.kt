package ru.mobileup.samples.features.audio.domain

import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.strResDesc
import ru.mobileup.samples.core.domain.DisplayedEnum
import ru.mobileup.samples.features.R

enum class AudioMenu(
    override val displayName: StringDesc
) : DisplayedEnum {
    Recorder(displayName = R.string.audio_menu_recorder.strResDesc())
}