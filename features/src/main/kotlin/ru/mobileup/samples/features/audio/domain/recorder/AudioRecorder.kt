package ru.mobileup.samples.features.audio.domain.recorder

import android.Manifest
import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.io.IOException
import ru.mobileup.samples.core.permissions.PermissionService
import ru.mobileup.samples.core.permissions.SinglePermissionResult
import ru.mobileup.samples.features.audio.domain.utils.getOutputAudioFile
import ru.mobileup.samples.features.audio.domain.wave.AudioAmplitudeGenerator

class AudioRecorder(
    private val context: Context,
    private val permissionService: PermissionService,
) {

    private val _recorderState = MutableStateFlow<AudioRecorderState>(AudioRecorderState.Idle)

    val recorderState = _recorderState.asStateFlow()

    val audioAmplitudeGenerator = AudioAmplitudeGenerator(recorderState)

    var recorder: MediaRecorder? = null
        private set

    suspend fun start() {
        if (verifyPermission()) {
            recorder = buildMediaRecorder()
                .apply {
                    try {
                        _recorderState.update { AudioRecorderState.Recording }
                        prepare()
                        start()
                    } catch (e: IOException) {
                        _recorderState.update { AudioRecorderState.Idle }
                    }
                }
            audioAmplitudeGenerator.startCollectAmplitudes { recorder }
        }
    }

    fun stop() {
        recorder?.stop()
        release()
        _recorderState.update { AudioRecorderState.Idle }
    }

    fun release() {
        recorder?.release()
        recorder = null
    }

    private suspend fun verifyPermission(): Boolean {
        return if (!permissionService.isPermissionGranted(RECORD_PERMISSION)) {
            permissionService.requestPermission(RECORD_PERMISSION) is SinglePermissionResult.Granted
        } else {
            true
        }
    }

    private fun buildMediaRecorder(): MediaRecorder {
        return createMediaRecorderInstance().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(context.getOutputAudioFile())
        }
    }

    private fun createMediaRecorderInstance(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context.applicationContext)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }
    }

    private companion object {
        const val RECORD_PERMISSION = Manifest.permission.RECORD_AUDIO
    }
}