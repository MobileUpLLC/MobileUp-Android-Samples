package ru.mobileup.samples.features.pin_code.presentation.widget

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.features.pin_code.domain.PinCode
import ru.mobileup.samples.features.pin_code.domain.PinCodeProgressState

private const val DOTS_ANIMATION_DURATION = 300L

@Composable
fun PinCodeProgress(
    pinProgressState: PinCodeProgressState,
    modifier: Modifier = Modifier,
    onDotsAnimationEnd: (() -> Unit)? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier.height(20.dp),
            contentAlignment = Alignment.Center
        ) {
            val coroutineScope = rememberCoroutineScope()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val progressAnimatables = remember {
                    List(PinCode.LENGTH) {
                        Animatable(1f)
                    }
                }

                val successAnimatable = remember {
                    Animatable(1f)
                }

                val errorAnimatable = remember {
                    Animatable(1f)
                }

                repeat(PinCode.LENGTH) { index ->
                    val (animatable, color) = when (pinProgressState) {
                        PinCodeProgressState.Error -> {
                            errorAnimatable to CustomTheme.colors.common.negative
                        }

                        is PinCodeProgressState.Progress -> {
                            val color = if (pinProgressState.count > index) {
                                CustomTheme.colors.icon.primary
                            } else {
                                CustomTheme.colors.icon.secondary
                            }
                            progressAnimatables[index] to color
                        }

                        PinCodeProgressState.Success -> {
                            successAnimatable to CustomTheme.colors.common.positive
                        }
                    }

                    ProgressDot(
                        color = color,
                        size = 12.dp,
                        scale = animatable.value
                    )

                    LaunchedEffect(pinProgressState) {
                        when (pinProgressState) {
                            PinCodeProgressState.Error -> coroutineScope.launch {
                                errorAnimatable.animateDot()
                                onDotsAnimationEnd?.invoke()
                            }
                            PinCodeProgressState.Success -> coroutineScope.launch {
                                successAnimatable.animateDot()
                                onDotsAnimationEnd?.invoke()
                            }

                            is PinCodeProgressState.Progress -> {
                                if (index + 1 == pinProgressState.count) {
                                    coroutineScope.launch {
                                        progressAnimatables[index].animateDot()
                                        if (pinProgressState.count == PinCode.LENGTH) {
                                            onDotsAnimationEnd?.invoke()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProgressDot(
    color: Color,
    size: Dp,
    scale: Float,
    modifier: Modifier = Modifier
) = Box(
    modifier = modifier
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clip(CircleShape)
        .size(size)
        .background(color)
)

private suspend fun Animatable<Float, AnimationVector1D>.animateDot() {
    animateBackAndForth(
        initialValue = 1f,
        targetValue = 1.33f,
        animationSpec = tween(
            durationMillis = DOTS_ANIMATION_DURATION.toInt()
        )
    )
}

private suspend fun <T, V : AnimationVector> Animatable<T, V>.animateBackAndForth(
    initialValue: T,
    targetValue: T,
    animationSpec: AnimationSpec<T>
) {
    snapTo(initialValue)
    animateTo(targetValue, animationSpec)
    animateTo(initialValue, animationSpec)
}
