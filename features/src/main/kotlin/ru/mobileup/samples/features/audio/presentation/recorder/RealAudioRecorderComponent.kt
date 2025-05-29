package ru.mobileup.samples.features.audio.presentation.recorder

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.essenty.lifecycle.doOnStop
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.mobileup.samples.core.error_handling.ErrorHandler
import ru.mobileup.samples.core.error_handling.safeLaunch
import ru.mobileup.samples.core.utils.componentScope
import ru.mobileup.samples.core.utils.computed
import ru.mobileup.samples.features.audio.data.repository.AudioRecordedFilesRepository
import ru.mobileup.samples.features.audio.domain.model.AudioFile
import ru.mobileup.samples.features.audio.domain.model.AudioFileId
import ru.mobileup.samples.features.audio.domain.model.PlayingAudioFile
import ru.mobileup.samples.features.audio.domain.player.AudioPlayer
import ru.mobileup.samples.features.audio.domain.player.AudioPlayerState
import ru.mobileup.samples.features.audio.domain.recorder.AudioRecorder
import ru.mobileup.samples.features.audio.domain.recorder.AudioRecorderState

class RealAudioRecorderComponent(
    componentContext: ComponentContext,
    private val audioRecorder: AudioRecorder,
    private val audioPlayer: AudioPlayer,
    private val errorHandler: ErrorHandler,
    private val audioRecordedFilesRepository: AudioRecordedFilesRepository,
) : ComponentContext by componentContext, AudioRecorderComponent {

    override val recorderState = audioRecorder.recorderState

    override val audioAmplitudeGenerator = audioRecorder.audioAmplitudeGenerator

    override val recordingFiles = audioRecordedFilesRepository
        .files
        .stateIn(
            scope = componentScope,
            initialValue = emptyList(),
            started = SharingStarted.Eagerly
        )

    private val selectedPlayingFile = MutableStateFlow<AudioFile?>(null)

    override val playingFile = computed(
        audioPlayer.playerState,
        selectedPlayingFile,
        audioPlayer.progressState
    ) { playerState, playingFile, playingProgress ->
        if (playingFile != null) {
            PlayingAudioFile(
                id = playingFile.id,
                playedDuration = playingProgress,
                isPlaying = playerState is AudioPlayerState.Playing
            )
        } else {
            null
        }
    }

    init {
        doOnDestroy {
            audioRecorder.release()
            audioPlayer.release()
        }

        doOnStop {
            if (audioRecorder.recorderState.value is AudioRecorderState.Recording) {
                audioRecorder.stop()
            }
            if (audioPlayer.playerState.value is AudioPlayerState.Playing) {
                audioPlayer.stop()
            }
        }
    }

    override fun onClickRecord() {
        componentScope.launch {
            if (playingFile.value?.isPlaying == true) {
                audioPlayer.stop()
            }
            if (audioRecorder.recorderState.value is AudioRecorderState.Recording) {
                audioRecorder.stop()
            } else {
                audioRecorder.start()
            }
        }
    }

    override fun onClickPlay(audioFileId: AudioFileId) {
        val playingFile = playingFile.value
        if (playingFile?.isPlaying == true) {
            audioPlayer.stop()
            if (audioFileId == playingFile.id) {
                return
            }
        }
        val intentFile = recordingFiles.value.find { it.id == audioFileId }
        if (intentFile != null) {
            val startDuration = if (intentFile.id == playingFile?.id) {
                playingFile.playedDuration
            } else {
                0
            }
            audioPlayer.play(
                file = intentFile,
                playedDurationMs = startDuration
            )
            selectedPlayingFile.value = intentFile
        }
    }

    override fun onClickDelete(audioFileId: AudioFileId) {
        componentScope.safeLaunch(
            errorHandler = errorHandler,
        ) {
            if (audioFileId == selectedPlayingFile.value?.id) {
                audioPlayer.stop()
            }
            audioRecordedFilesRepository.delete(audioFileId)
        }
    }
}