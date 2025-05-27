package ru.mobileup.samples.features.audio.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import ru.mobileup.samples.core.utils.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.Children
import ru.mobileup.samples.features.audio.presentation.menu.AudioMenuUi
import ru.mobileup.samples.features.audio.presentation.recorder.AudioRecorderUi

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun AudioUi(
    component: AudioComponent,
    modifier: Modifier = Modifier
) {
    val childStack by component.childStack.collectAsState()

    Children(
        modifier = modifier,
        stack = childStack,
        animation = component.predictiveBackAnimation(),
    ) { child ->
        when (val instance = child.instance) {
            is AudioComponent.Child.Menu -> AudioMenuUi(instance.component)
            is AudioComponent.Child.Recorder -> AudioRecorderUi(instance.component)
        }
    }
}