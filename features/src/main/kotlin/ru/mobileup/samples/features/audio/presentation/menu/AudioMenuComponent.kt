package ru.mobileup.samples.features.audio.presentation.menu

import ru.mobileup.samples.features.audio.domain.AudioMenu

interface AudioMenuComponent {

    fun onClickMenuItem(audioMenu: AudioMenu)

    sealed interface Output {
        data class OnMenuChoice(val audioMenu: AudioMenu) : Output
    }
}