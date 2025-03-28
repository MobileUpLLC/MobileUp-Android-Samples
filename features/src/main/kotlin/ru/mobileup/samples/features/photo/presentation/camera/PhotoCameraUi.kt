package ru.mobileup.samples.features.photo.presentation.camera

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.camera.core.FocusMeteringAction
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.viewinterop.AndroidView
import coil3.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.utils.SystemBars
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.photo.domain.events.PhotoCameraEvent
import ru.mobileup.samples.features.photo.domain.states.CameraState
import ru.mobileup.samples.features.photo.presentation.camera.controller.PhotoCameraController
import ru.mobileup.samples.features.photo.presentation.camera.widget.CameraButton
import ru.mobileup.samples.features.photo.presentation.camera.widget.TorchSwitchIcon
import ru.mobileup.samples.features.video.presentation.recorder.widgets.CameraFlipIcon
import ru.mobileup.samples.features.video.presentation.recorder.widgets.FocusIndicator
import java.util.concurrent.TimeUnit

private const val ASPECT_RATIO_16_9 = 9 / 16f

@Composable
fun PhotoCameraUi(
    component: PhotoCameraComponent,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val cameraState by component.cameraState.collectAsState()

    val previewView by remember(context, lifecycleOwner) {
        mutableStateOf(
            PreviewView(context).apply {
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                scaleType = PreviewView.ScaleType.FIT_CENTER
            }
        )
    }

    var cameraPlaceHolder: Pair<Bitmap?, Boolean> by remember {
        mutableStateOf(null to false)
    }

    val photoCameraController by remember(context, lifecycleOwner, previewView) {
        mutableStateOf(
            PhotoCameraController(
                context = context,
                lifecycleOwner = lifecycleOwner,
                previewView = previewView,
                onPhotoCameraEvent = {
                    when (it) {
                        is PhotoCameraEvent.PhotoCaptured -> component.onPhotoTaken(it.uri)
                        is PhotoCameraEvent.Error -> component.onPhotoFailed()
                    }
                },
                onCameraInitializationFailed = {
                    component.onCameraInitializationFailed()
                },
                onPlaceHolderUpdated = {
                    cameraPlaceHolder = Pair(
                        first = it ?: cameraPlaceHolder.first,
                        second = it != null
                    )
                }
            )
        )
    }

    SystemBars(
        transparentNavigationBar = true,
        lightStatusBarIcons = true
    )

    LaunchedEffect(cameraState) {
        photoCameraController.cameraState = cameraState
    }

    PhotoCameraContent(
        modifier = modifier,
        component = component,
        cameraState = cameraState,
        previewView = previewView,
        cameraPlaceHolder = cameraPlaceHolder,
        photoCameraController = photoCameraController
    )
}

@Composable
private fun PhotoCameraContent(
    component: PhotoCameraComponent,
    cameraState: CameraState,
    previewView: PreviewView,
    cameraPlaceHolder: Pair<Bitmap?, Boolean>,
    photoCameraController: PhotoCameraController,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    val blurStrength = remember { Animatable(0f) }

    val configuration = LocalConfiguration.current

    var orientation by remember { mutableIntStateOf(Configuration.ORIENTATION_PORTRAIT) }

    LaunchedEffect(configuration) {
        snapshotFlow { configuration.orientation }
            .collect { orientation = it }
    }

    fun animateBlur() {
        scope.launch {
            blurStrength.snapTo(0f)
            blurStrength.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }

    Box(modifier = modifier.fillMaxSize().background(CustomTheme.colors.palette.black)) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            CameraLandscapeContent(
                component = component,
                cameraState = cameraState,
                previewView = previewView,
                cameraPlaceHolder = cameraPlaceHolder,
                photoCameraController = photoCameraController,
                blur = blurStrength.value,
                onBlurChange = ::animateBlur
            )
        } else {
            CameraPortraitContent(
                component = component,
                cameraState = cameraState,
                previewView = previewView,
                cameraPlaceHolder = cameraPlaceHolder,
                photoCameraController = photoCameraController,
                blur = blurStrength.value,
                onBlurChange = ::animateBlur
            )
        }
    }
}

@Composable
fun CameraPortraitContent(
    component: PhotoCameraComponent,
    cameraState: CameraState,
    previewView: PreviewView,
    cameraPlaceHolder: Pair<Bitmap?, Boolean>,
    photoCameraController: PhotoCameraController,
    blur: Float,
    onBlurChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    var isPhotoBlackoutVisible by remember {
        mutableStateOf(false)
    }

    Column(modifier = modifier.fillMaxSize()) {
        CameraHeader(Configuration.ORIENTATION_PORTRAIT)

        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(ASPECT_RATIO_16_9)
                    .align(Alignment.CenterStart)
            ) {
                CameraPreview(
                    previewView = previewView,
                    cameraPlaceHolder = cameraPlaceHolder,
                    photoCameraController = photoCameraController,
                    blur = blur
                )

                if (isPhotoBlackoutVisible) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(CustomTheme.colors.palette.black)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CustomTheme.colors.palette.black.copy(alpha = 0.3f))
                        .padding(top = 16.dp, bottom = 24.dp)
                        .height(intrinsicSize = IntrinsicSize.Max)
                        .align(Alignment.BottomStart)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        TorchSwitchIcon(
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .align(Alignment.CenterEnd),
                            torchState = cameraState.torchState,
                            onClick = component::onUpdateTorchState,
                        )
                    }

                    CameraButton(
                        onClick = {
                            photoCameraController.takePhoto()

                            scope.launch {
                                isPhotoBlackoutVisible = true
                                delay(100)
                                isPhotoBlackoutVisible = false
                            }
                        }
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        CameraFlipIcon(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .align(Alignment.CenterStart),
                            onClick = {
                                component.onFlipCameraSelector()
                                onBlurChange()
                            }
                        )

                        cameraState.mediaList.lastOrNull()?.let { uri ->
                            Image(
                                painter = rememberAsyncImagePainter(uri),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .padding(end = 24.dp)
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .border(width = 1.dp, color = Color.White, CircleShape)
                                    .align(Alignment.CenterEnd)
                                    .clickable { component.onShowPreview() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CameraLandscapeContent(
    component: PhotoCameraComponent,
    cameraState: CameraState,
    previewView: PreviewView,
    cameraPlaceHolder: Pair<Bitmap?, Boolean>,
    photoCameraController: PhotoCameraController,
    blur: Float,
    onBlurChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    var isPhotoBlackoutVisible by remember {
        mutableStateOf(false)
    }

    Row(modifier = modifier.fillMaxSize()) {
        CameraHeader(Configuration.ORIENTATION_LANDSCAPE)

        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1 / ASPECT_RATIO_16_9)
                    .align(Alignment.TopCenter)
            ) {
                CameraPreview(
                    previewView = previewView,
                    cameraPlaceHolder = cameraPlaceHolder,
                    photoCameraController = photoCameraController,
                    blur = blur
                )

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(CustomTheme.colors.palette.black.copy(alpha = 0.3f))
                        .padding(start = 16.dp, end = 24.dp)
                        .width(intrinsicSize = IntrinsicSize.Max)
                        .align(Alignment.TopEnd)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        cameraState.mediaList.lastOrNull()?.let { uri ->
                            Image(
                                painter = rememberAsyncImagePainter(uri),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .padding(top = 24.dp)
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .border(width = 1.dp, color = Color.White, CircleShape)
                                    .align(Alignment.TopCenter)
                                    .clickable { component.onShowPreview() }
                            )
                        }

                        CameraFlipIcon(
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .align(Alignment.BottomCenter),
                            onClick = {
                                component.onFlipCameraSelector()
                                onBlurChange()
                            }
                        )
                    }

                    CameraButton(
                        onClick = {
                            photoCameraController.takePhoto()

                            scope.launch {
                                isPhotoBlackoutVisible = true
                                delay(100)
                                isPhotoBlackoutVisible = false
                            }
                        }
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        TorchSwitchIcon(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .align(Alignment.TopCenter),
                            torchState = cameraState.torchState,
                            onClick = component::onUpdateTorchState,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CameraHeader(orientation: Int) {
    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(CustomTheme.colors.palette.black)
                .padding(vertical = 8.dp, horizontal = 24.dp)
                .padding(end = 16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_app_logo),
                contentDescription = "logo",
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.weight(1f))
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CustomTheme.colors.palette.black)
                .padding(horizontal = 8.dp, vertical = 24.dp)
                .padding(top = 16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_app_logo),
                contentDescription = "logo",
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = stringResource(R.string.menu_item_camera),
                color = CustomTheme.colors.text.invert,
                modifier = Modifier
                    .weight(2f)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
private fun CameraPreview(
    previewView: PreviewView,
    cameraPlaceHolder: Pair<Bitmap?, Boolean>,
    photoCameraController: PhotoCameraController,
    blur: Float
) {
    var focusPoint by remember {
        mutableStateOf(Offset(-1f, -1f))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    val factory = previewView.meteringPointFactory
                    val point = factory.createPoint(it.x, it.y)
                    val action = FocusMeteringAction.Builder(point).apply {
                        setAutoCancelDuration(3, TimeUnit.SECONDS)
                    }.build()
                    photoCameraController.focusChange(action)
                    focusPoint = it
                }
            }
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize(),
            factory = remember { { previewView } },
        )

        AnimatedVisibility(
            visible = cameraPlaceHolder.second,
            enter = EnterTransition.None,
            exit = fadeOut()
        ) {
            cameraPlaceHolder.first?.let {
                Image(
                    modifier = Modifier.fillMaxSize().blur((blur * 32.dp)),
                    bitmap = it.asImageBitmap(),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }
        }

        FocusIndicator(
            offset = focusPoint,
            onExposureChange = {
                photoCameraController.exposureChange(it)
            }
        )
    }
}

@Preview
@Composable
private fun MenuUiPreview() {
    AppTheme {
        PhotoCameraUi(FakePhotoCameraComponent())
    }
}