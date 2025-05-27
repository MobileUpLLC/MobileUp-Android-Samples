package ru.mobileup.samples.features.audio.presentation.recorder

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.utils.SystemBars
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.audio.domain.recorder.AudioRecorderState
import ru.mobileup.samples.features.audio.presentation.widget.AudioFilesList
import ru.mobileup.samples.features.audio.presentation.widget.AudioRecordWave

@Composable
fun AudioRecorderUi(
    component: AudioRecorderComponent,
    modifier: Modifier = Modifier
) {
    val recordState by component.recorderState.collectAsState()

    SystemBars(statusBarColor = Color.Transparent)

    Column(
        modifier = modifier
    ) {

        val recordingFiles by component.recordingFiles.collectAsState()

        val playingFile by component.playingFile.collectAsState()

        AudioFilesList(
            files = recordingFiles,
            playingFile = playingFile,
            onClick = component::onClickPlay,
            onDelete = component::onClickDelete,
            contentPadding = PaddingValues(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 20.dp,
                bottom = 20.dp
            ),
            modifier = Modifier
                .weight(0.7f)
                .padding(horizontal = 20.dp)
        )

        Spacer(
            Modifier
                .fillMaxWidth(0.95f)
                .height(4.dp)
                .align(Alignment.CenterHorizontally)
                .background(
                    color = CustomTheme.colors.chat.secondary,
                    shape = CircleShape
                )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f),
            contentAlignment = Alignment.Center
        ) {
            Column {
                AudioRecordWave(
                    amplitudeHolder = component.audioAmplitudeGenerator,
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )

                val animatedIconColor by animateColorAsState(
                    targetValue = when (recordState) {
                        AudioRecorderState.Idle -> CustomTheme.colors.button.primary
                        AudioRecorderState.Recording -> CustomTheme.colors.common.positive
                    },
                    animationSpec = tween(800)
                )

                val scaleInfiniteTransition = rememberInfiniteTransition()

                val scale = scaleInfiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = when (recordState) {
                        AudioRecorderState.Idle -> 1.0f
                        AudioRecorderState.Recording -> 1.5f
                    },
                    animationSpec = infiniteRepeatable(
                        animation = tween(800),
                        repeatMode = RepeatMode.Reverse
                    )
                )

                IconButton(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .size(64.dp)
                        .align(Alignment.CenterHorizontally)
                        .graphicsLayer {
                            scaleX = scale.value
                            scaleY = scale.value
                        },
                    shape = CircleShape,
                    onClick = component::onClickRecord,
                ) {
                    Icon(
                        modifier = Modifier
                            .size(48.dp),
                        painter = painterResource(R.drawable.ic_mic),
                        contentDescription = null,
                        tint = animatedIconColor
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun AudioRecorderUiPreview() {
    AppTheme {
        AudioRecorderUi(FakeAudioRecorderComponent(), Modifier.fillMaxSize())
    }
}