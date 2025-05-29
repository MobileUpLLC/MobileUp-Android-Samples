package ru.mobileup.samples.features.audio.presentation.recorder

import kotlinx.coroutines.flow.MutableStateFlow
import ru.mobileup.samples.features.audio.domain.model.AudioFile
import ru.mobileup.samples.features.audio.domain.model.AudioFileId
import ru.mobileup.samples.features.audio.domain.model.PlayingAudioFile
import ru.mobileup.samples.features.audio.domain.recorder.AudioRecorderState
import ru.mobileup.samples.features.audio.domain.wave.AudioAmplitudeGenerator

class FakeAudioRecorderComponent : AudioRecorderComponent {
    override val recorderState = MutableStateFlow(AudioRecorderState.Idle)
    override val audioAmplitudeGenerator: AudioAmplitudeGenerator = AudioAmplitudeGenerator(recorderState)
    override val recordingFiles = MutableStateFlow(AudioFile.MOCKS)
    override val playingFile = MutableStateFlow<PlayingAudioFile?>(null)

    override fun onClickRecord() = Unit
    override fun onClickPlay(audioFileId: AudioFileId) = Unit

    override fun onClickDelete(audioFileId: AudioFileId) = Unit
}