package ru.mobileup.samples.features.audio.presentation.widget

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.delay
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.utils.formatMillisToMS
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.audio.domain.model.AudioFile
import ru.mobileup.samples.features.audio.domain.model.AudioFileId
import ru.mobileup.samples.features.audio.domain.model.PlayingAudioFile
import ru.mobileup.samples.features.audio.domain.utils.displayedTime

private const val DELETE_POPUP_DELAY = 5_000L
private const val DELETE_SWIPE_RESET_DELAY = 1_500L

@Composable
fun AudioFilesList(
    files: List<AudioFile>,
    onClick: (AudioFileId) -> Unit,
    onDelete: (AudioFileId) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(vertical = 16.dp),
    playingFile: PlayingAudioFile? = null,
) {
    val lazyState = rememberLazyListState()

    var itemsCount by remember { mutableIntStateOf(files.size) }

    val onDeleteUpdated by rememberUpdatedState(onDelete)

    var showDeleteHelpPopup by remember { mutableStateOf(true) }

    LaunchedEffect(files.size) {
        if (files.size > itemsCount) {
            lazyState.animateScrollToItem(0)
        }
        itemsCount = files.size
    }

    Box(
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = lazyState,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = contentPadding
        ) {
            items(
                files,
                key = { it.id.value }
            ) {
                val swipeState = rememberSwipeToDismissBoxState()

                LaunchedEffect(swipeState.currentValue) {
                    if (swipeState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                        onDeleteUpdated(it.id)
                        delay(DELETE_SWIPE_RESET_DELAY)
                        swipeState.reset()
                    }
                }

                SwipeToDismissBox(
                    modifier = Modifier
                        .animateItem(),
                    state = swipeState,
                    enableDismissFromStartToEnd = false,
                    backgroundContent = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterVertically)
                                .padding(20.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = CustomTheme.colors.common.negative,
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .size(24.dp)
                                    .graphicsLayer {
                                        val scale = lerp(0.9f, 1.4f, swipeState.progress)
                                        scaleX = scale
                                        scaleY = scale
                                    }
                            )
                        }
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        AudioFileUi(
                            file = it,
                            isPlaying = it.id == playingFile?.id && playingFile.isPlaying,
                            progress = {
                                if (it.id == playingFile?.id) {
                                    playingFile.playedDuration.toFloat() / it.duration.toFloat()
                                } else {
                                    0f
                                }
                            },
                            onClick = { onClick(it.id) },
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(CustomTheme.colors.chat.secondary)
                                .padding(vertical = 8.dp, horizontal = 16.dp)
                        )

                        Text(
                            text = it.lastModified.displayedTime(),
                            style = CustomTheme.typography.caption.regular,
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(horizontal = 15.dp)
                        )
                    }
                }
            }
        }

        if (showDeleteHelpPopup && files.isNotEmpty()) {

            LaunchedEffect(Unit) {
                delay(DELETE_POPUP_DELAY)
                showDeleteHelpPopup = false
            }

            Popup(
                offset = IntOffset(x = 0, y = -25),
                alignment = Alignment.BottomEnd,
                onDismissRequest = { showDeleteHelpPopup = false }
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Surface(
                        modifier = Modifier
                            .widthIn(max = 200.dp)
                            .padding(end = 16.dp),
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 16.dp
                        ),
                        shadowElevation = 2.dp
                    ) {
                        Text(
                            text = stringResource(R.string.audio_recorder_delete_help_message),
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    Icon(
                        modifier = Modifier
                            .align(Alignment.End),
                        painter = painterResource(R.drawable.ic_info),
                        contentDescription = "delete_popup_info",
                        tint = CustomTheme.colors.text.primary
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AudioFileUi(
    file: AudioFile,
    isPlaying: Boolean,
    progress: () -> Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = formatMillisToMS(((1f - progress()) * file.duration).toLong()),
            color = CustomTheme.colors.button.primary,
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        Slider(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            track = { sliderState ->
                SliderDefaults.Track(
                    modifier = Modifier.height(4.dp),
                    sliderState = sliderState,
                    thumbTrackGapSize = 0.dp,
                    colors = SliderDefaults.colors().copy(
                        activeTrackColor = CustomTheme.colors.button.primary,
                        inactiveTrackColor = Color.White.copy(0.7f),
                    ),
                    drawStopIndicator = null
                )
            },
            thumb = { /*Nothing*/ },
            value = progress(),
            onValueChange = { }
        )

        IconButton(
            modifier = Modifier
                .size(32.dp),
            shape = CircleShape,
            onClick = onClick,
        ) {
            Crossfade(
                targetState = isPlaying
            ) { isPlay ->
                Icon(
                    modifier = Modifier
                        .size(24.dp),
                    painter = if (isPlay) {
                        painterResource(R.drawable.ic_pause)
                    } else {
                        painterResource(R.drawable.ic_play)
                    },
                    contentDescription = null,
                    tint = CustomTheme.colors.button.primary
                )
            }
        }
    }
}

@Composable
@Preview
private fun AudioFilesListPreview() {
    AppTheme {
        AudioFilesList(
            files = AudioFile.MOCKS,
            onClick = {},
            onDelete = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}