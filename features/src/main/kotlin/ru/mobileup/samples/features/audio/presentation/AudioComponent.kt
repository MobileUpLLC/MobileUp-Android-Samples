package ru.mobileup.samples.features.audio.presentation

import com.arkivanov.decompose.router.stack.ChildStack
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.utils.PredictiveBackComponent
import ru.mobileup.samples.features.audio.presentation.menu.AudioMenuComponent
import ru.mobileup.samples.features.audio.presentation.recorder.AudioRecorderComponent

interface AudioComponent : PredictiveBackComponent {

    val childStack: StateFlow<ChildStack<*, Child>>

    sealed interface Child {
        data class Menu(val component: AudioMenuComponent) : Child
        data class Recorder(val component: AudioRecorderComponent) : Child
    }
}