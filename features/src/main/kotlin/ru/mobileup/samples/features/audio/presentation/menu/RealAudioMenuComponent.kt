package ru.mobileup.samples.features.audio.presentation.menu

import com.arkivanov.decompose.ComponentContext
import ru.mobileup.samples.features.audio.domain.AudioMenu

class RealAudioMenuComponent(
    componentContext: ComponentContext,
    private val onOutput: (AudioMenuComponent.Output) -> Unit
) : ComponentContext by componentContext, AudioMenuComponent {

    override fun onClickMenuItem(audioMenu: AudioMenu) {
        onOutput(AudioMenuComponent.Output.OnMenuChoice(audioMenu))
    }
}