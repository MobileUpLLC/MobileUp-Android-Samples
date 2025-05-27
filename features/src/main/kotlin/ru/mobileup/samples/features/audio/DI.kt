package ru.mobileup.samples.features.audio

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import org.koin.dsl.module
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.features.audio.data.repository.AudioRecordedFilesRepository
import ru.mobileup.samples.features.audio.data.repository.AudioRecordedFilesRepositoryImpl
import ru.mobileup.samples.features.audio.domain.player.AudioPlayer
import ru.mobileup.samples.features.audio.domain.recorder.AudioRecorder
import ru.mobileup.samples.features.audio.presentation.AudioComponent
import ru.mobileup.samples.features.audio.presentation.RealAudioComponent
import ru.mobileup.samples.features.audio.presentation.menu.AudioMenuComponent
import ru.mobileup.samples.features.audio.presentation.menu.RealAudioMenuComponent
import ru.mobileup.samples.features.audio.presentation.recorder.AudioRecorderComponent
import ru.mobileup.samples.features.audio.presentation.recorder.RealAudioRecorderComponent

val audioModule = module {
    factory { AudioRecorder(get(), get()) }
    factory { AudioPlayer(get()) }
    single<AudioRecordedFilesRepository> {
        AudioRecordedFilesRepositoryImpl(get())
    }
}

fun ComponentFactory.createAudioComponent(
    componentContext: ComponentContext
): AudioComponent = RealAudioComponent(
    componentContext,
    get()
)

fun ComponentFactory.createAudioMenuComponent(
    componentContext: ComponentContext,
    onOutput: (AudioMenuComponent.Output) -> Unit
): AudioMenuComponent = RealAudioMenuComponent(
    componentContext,
    onOutput
)

fun ComponentFactory.createAudioRecordedComponent(
    componentContext: ComponentContext,
): AudioRecorderComponent = RealAudioRecorderComponent(
    componentContext,
    get(),
    get(),
    get(),
    get()
)