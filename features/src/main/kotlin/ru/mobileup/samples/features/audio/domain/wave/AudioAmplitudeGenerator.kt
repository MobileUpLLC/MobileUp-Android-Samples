package ru.mobileup.samples.features.audio.domain.wave

import android.media.MediaRecorder
import androidx.compose.runtime.Stable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.mobileup.samples.features.audio.domain.recorder.AudioAmplitude
import ru.mobileup.samples.features.audio.domain.recorder.AudioRecorderState

@Stable
class AudioAmplitudeGenerator(
    private val recorderState: StateFlow<AudioRecorderState>
) {

    private val _amplitudesFlow = MutableSharedFlow<AudioAmplitude>()

    val amplitudesFlow = _amplitudesFlow.asSharedFlow()

    suspend fun startCollectAmplitudes(recorder: () -> MediaRecorder?) {
        try {
            while (recorderState.value is AudioRecorderState.Recording) {
                val currentRecorder = recorder() ?: break
                val amplitude = currentRecorder.maxAmplitude
                _amplitudesFlow.emit(
                    AudioAmplitude(
                        value = amplitude.toFloat() / MAX_AMPLITUDE_VALUE.toFloat()
                    )
                )
                delay(AMPLITUDE_EMIT_DELAY)
            }
        } catch (e: Exception) { return }
    }

    private companion object {
        const val MAX_AMPLITUDE_VALUE = Short.MAX_VALUE
        const val AMPLITUDE_EMIT_DELAY = 100L
    }
}