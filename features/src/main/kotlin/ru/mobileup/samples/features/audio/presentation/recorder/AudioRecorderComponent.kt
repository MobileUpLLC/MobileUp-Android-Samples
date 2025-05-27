package ru.mobileup.samples.features.audio.presentation.recorder

import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.features.audio.domain.model.AudioFile
import ru.mobileup.samples.features.audio.domain.model.AudioFileId
import ru.mobileup.samples.features.audio.domain.model.PlayingAudioFile
import ru.mobileup.samples.features.audio.domain.recorder.AudioRecorderState
import ru.mobileup.samples.features.audio.domain.wave.AudioAmplitudeGenerator

interface AudioRecorderComponent {

    val recorderState: StateFlow<AudioRecorderState>

    val audioAmplitudeGenerator: AudioAmplitudeGenerator

    val recordingFiles: StateFlow<List<AudioFile>>

    val playingFile: StateFlow<PlayingAudioFile?>

    fun onClickRecord()

    fun onClickPlay(audioFileId: AudioFileId)

    fun onClickDelete(audioFileId: AudioFileId)
}