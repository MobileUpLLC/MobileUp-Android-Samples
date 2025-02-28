package ru.mobileup.samples.features.video.presentation.recorder.controller

import android.content.Context
import android.graphics.Bitmap
import android.util.Range
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraEffect
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.MirrorMode
import androidx.camera.core.Preview
import androidx.camera.core.UseCaseGroup
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.util.Consumer
import androidx.lifecycle.LifecycleOwner
import co.touchlab.kermit.Logger
import ru.mobileup.samples.features.video.data.render.GlFilter
import ru.mobileup.samples.features.video.data.render.camera.ReleasableCameraEffect
import ru.mobileup.samples.features.video.data.render.camera.ShaderCameraEffect
import ru.mobileup.samples.features.video.data.utils.startRecording
import ru.mobileup.samples.features.video.domain.events.CameraEvent
import ru.mobileup.samples.features.video.domain.events.RecordingError
import ru.mobileup.samples.features.video.domain.events.RecordingResult
import ru.mobileup.samples.features.video.domain.states.RecorderState
import kotlin.math.abs

private const val TAG = "Camera"

class CameraController(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val previewView: PreviewView,
    private val onCameraRecordingEvent: (CameraEvent) -> Unit,
    private val onCameraInitializationFailed: () -> Unit,
    private val onPlaceHolderUpdated: (Bitmap?) -> Unit
) {
    private val cameraProvider = ProcessCameraProvider.getInstance(context).get()

    private var cameraLink: Camera? = null
    private var videoCapture: VideoCapture<Recorder>? = null
    private var isReleased = false
    private var exposureRange: Range<Int> = Range(0, 0)

    private var recording: Recording? = null
    private var recordingInProgress: Boolean = false
    private var cameraEffect: CameraEffect? = null
    private var zoomValue = 1f

    private val recordingListener = Consumer<VideoRecordEvent> { event ->
        when (event) {
            is VideoRecordEvent.Start -> {
                Logger.withTag(TAG).d("Record start")
                onCameraRecordingEvent(CameraEvent.StartRecord)
            }

            is VideoRecordEvent.Finalize -> {
                // Record doesn't have errors or it was stopped because the source stopped sending frames
                val recordingResult: RecordingResult
                if (
                    !event.hasError() || event.error == VideoRecordEvent.Finalize.ERROR_SOURCE_INACTIVE ||
                    event.error == VideoRecordEvent.Finalize.ERROR_DURATION_LIMIT_REACHED
                ) {
                    // Record completed
                    Logger.withTag(TAG)
                        .d("Record completed, output ${event.outputResults.outputUri}")
                    recordingResult = RecordingResult.Success(uri = event.outputResults.outputUri)
                } else {
                    // Record failed
                    Logger.withTag(TAG).d("Record failed, error code ${event.error} ${event.cause}")
                    recordingResult = RecordingResult.Error(error = RecordingError.Another)
                }
                onCameraRecordingEvent(CameraEvent.StopRecord(recordingResult))
            }

            is VideoRecordEvent.Status -> {
                val durationInMillis = event.recordingStats.recordedDurationNanos / 1_000_000
                onCameraRecordingEvent(CameraEvent.ProgressRecord(recordDuration = durationInMillis))
            }

            else -> {
                Logger.withTag(TAG).d(event.toString())
            }
        }
    }

    var glFilter: GlFilter = GlFilter.Default
        set(value) {
            field = value
            (cameraEffect as? ShaderCameraEffect)?.setGlFilter(value)
        }

    var recorderState = RecorderState.build()
        set(value) {
            // Rebuild only if we changed the camera configuration
            if (field.cameraSelector != value.cameraSelector ||
                field.fps != value.fps ||
                field.quality != value.quality
            ) {
                val isCameraUpdated = field.cameraSelector != value.cameraSelector

                field = value

                onPlaceHolderUpdated(previewView.bitmap)

                setupVideoUseCase()

                if (isCameraUpdated) {
                    changeTorchState(value.torchState)
                }
            }
        }

    init {
        setupVideoUseCase()
    }

    fun startRecording() {
        if (recordingInProgress) return
        if (isReleased) return
        val videoCapture = videoCapture ?: return

        recordingInProgress = true
        recording = context.startRecording(
            videoCapture = videoCapture,
            consumer = recordingListener,
            withAudio = true,
            limitTimeMillis = 60_000
        )
    }

    fun zoomChange(zoomChange: Float) {
        zoomValue *= zoomChange
        cameraLink?.cameraControl?.setZoomRatio(zoomValue)
    }

    fun focusChange(focusMeteringAction: FocusMeteringAction) {
        cameraLink?.cameraControl?.startFocusAndMetering(focusMeteringAction)
    }

    fun exposureChange(exposure: Float) {
        val newExposure = when {
            exposure > 0 -> {
                exposureRange.upper * exposure
            }

            exposure < 0 -> {
                exposureRange.lower * abs(exposure)
            }

            else -> 0
        }.toInt()

        cameraLink?.cameraControl?.setExposureCompensationIndex(newExposure)
    }

    fun changeTorchState(enabled: Boolean) {
        if (cameraLink?.cameraInfo?.hasFlashUnit() == true &&
            recorderState.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA
        ) {
            cameraLink?.cameraControl?.enableTorch(enabled)
        }
    }

    fun stopRecording() {
        recordingInProgress = false
        recording?.stop()
    }

    fun release() {
        (cameraEffect as? ReleasableCameraEffect)?.release()
        cameraLink = null
        cameraProvider.unbindAll()
        isReleased = true
    }

    private fun setupVideoUseCase() {
        if (isReleased) return
        videoCapture = createVideoCaptureUseCase()
    }

    private fun createVideoCaptureUseCase(): VideoCapture<Recorder>? {
        val executor = ContextCompat.getMainExecutor(context)
        cameraProvider.unbindAll()

        val preview = Preview.Builder()
            .build()
            .apply {
                surfaceProvider = previewView.surfaceProvider
            }

        val recorder = Recorder.Builder()
            .setExecutor(executor)
            .setAspectRatio(AspectRatio.RATIO_16_9)
            .setQualitySelector(QualitySelector.from(recorderState.quality))
            .build()

        val videoCapture = VideoCapture.Builder(recorder)
            .setMirrorMode(MirrorMode.MIRROR_MODE_ON_FRONT_ONLY)
            .setTargetFrameRate(Range(30, recorderState.fps))
            .build()

        (cameraEffect as? ReleasableCameraEffect)?.release()

        cameraEffect = ShaderCameraEffect.create().also {
            it.setGlFilter(glFilter)
        }

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context)) {
            onPlaceHolderUpdated(null)
            imageAnalysis.clearAnalyzer()
        }

        val useCaseGroup = UseCaseGroup.Builder()
            .addUseCase(preview)
            .addUseCase(videoCapture)
            .addUseCase(imageAnalysis)
            .apply {
                cameraEffect?.let { addEffect(it) }
            }
            .build()

        try {
            cameraProvider.bindToLifecycle(
                lifecycleOwner = lifecycleOwner,
                cameraSelector = recorderState.cameraSelector,
                useCaseGroup = useCaseGroup
            ).also {
                cameraLink = it
                exposureRange = it.cameraInfo.exposureState.exposureCompensationRange
            }
        } catch (e: Exception) {
            Logger.withTag(TAG).d("Camera initialization failed: $e")
            onCameraInitializationFailed()
            return null
        }

        return videoCapture
    }
}