package ru.mobileup.samples.features.video.presentation.player.widgets

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.features.R

private const val ANIMATION_DURATION_MS = 500

@Immutable
data class PlayerIndicationState(
    val alpha: Float,
    val isPlaying: Boolean
)

@Composable
fun PlayerIndication(
    state: PlayerIndicationState,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true
) {
    if (isVisible) {
        Box(
            modifier = modifier
                .size(60.dp)
                .clip(CircleShape)
                .alpha(state.alpha)
                .background(CustomTheme.colors.palette.black50)

        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(24.dp)
                    .iconShadow(),
                painter = painterResource(
                    if (state.isPlaying) {
                        R.drawable.ic_pause
                    } else {
                        R.drawable.ic_play
                    }
                ),
                tint = CustomTheme.colors.palette.white,
                contentDescription = null,
            )
        }
    }
}

@Composable
fun rememberPlayerIndicationState(isPlaying: Boolean): State<PlayerIndicationState> {
    val animatePlayButton = remember { Animatable(0f) }
    var isFirstLaunch by remember { mutableStateOf(true) }

    LaunchedEffect(isPlaying) {
        if (isFirstLaunch) {
            isFirstLaunch = false
            return@LaunchedEffect
        }

        animatePlayButton.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = ANIMATION_DURATION_MS,
                easing = LinearEasing
            )
        )

        animatePlayButton.animateTo(
            targetValue = 0f,
            animationSpec = tween(
                durationMillis = ANIMATION_DURATION_MS,
                delayMillis = ANIMATION_DURATION_MS,
                easing = LinearEasing
            )
        )
    }

    return remember(isPlaying, animatePlayButton.value) {
        mutableStateOf(
            PlayerIndicationState(
                isPlaying = isPlaying,
                alpha = animatePlayButton.value
            )
        )
    }
}

@Suppress("ModifierComposable")
@Composable
fun Modifier.iconShadow() = this.then(
    Modifier.background(
        brush = Brush.radialGradient(
            colors = listOf(
                CustomTheme.colors.palette.black10,
                Color.Transparent
            )
        ),
        shape = RoundedCornerShape(40.dp)
    )
)

@Preview
@Composable
private fun PlayerIndicationPreview() {
    AppTheme {
        val playingIndicationState by rememberPlayerIndicationState(isPlaying = true)
        val pausedIndicationState by rememberPlayerIndicationState(isPlaying = false)

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            PlayerIndication(
                modifier = Modifier.fillMaxWidth(),
                state = playingIndicationState,
                isVisible = true
            )

            PlayerIndication(
                modifier = Modifier.fillMaxWidth(),
                state = pausedIndicationState,
                isVisible = true
            )
        }
    }
}