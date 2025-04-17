package ru.mobileup.samples.features.map.presentation.widgets

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.mobileup.samples.core.R
import ru.mobileup.samples.core.map.domain.MapTheme
import ru.mobileup.samples.core.utils.clickableNoRipple
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MapThemeSwitch(
    theme: MapTheme,
    onThemeSwitch: (MapTheme) -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val iconsSize = remember { 48.dp }
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()

    val backgroundColor by animateColorAsState(
        when (theme) {
            MapTheme.Dark -> Color(0xFF2C2C2C).copy(alpha = 0.8f)
            MapTheme.Default -> Color(0xFFEFEFEF).copy(alpha = 0.8f)
            MapTheme.Bright -> Color.White.copy(alpha = 0.8f)
        }
    )

    val borderColor by animateColorAsState(
        when (theme) {
            MapTheme.Dark -> Color(0xFF444444)
            else -> Color.White
        }
    )

    val iconTint by animateColorAsState(
        when (theme) {
            MapTheme.Dark -> Color.White
            else -> Color.Black
        }
    )

    val handleColor by animateColorAsState(
        when (theme) {
            MapTheme.Dark -> Color.White.copy(alpha = 0.3f)
            else -> Color.Black.copy(alpha = 0.2f)
        }
    )

    val dragState = remember {
        AnchoredDraggableState(
            initialValue = theme,
            anchors = DraggableAnchors {
                MapTheme.Bright at 0f
                MapTheme.Default at with(density) { iconsSize.toPx() }
                MapTheme.Dark at with(density) { iconsSize.toPx() * 2 }
            },
            positionalThreshold = { it / 2 },
            velocityThreshold = { with(density) { iconsSize.toPx() } },
            snapAnimationSpec = spring(),
            decayAnimationSpec = decayAnimationSpec,
        )
    }

    val currentOnThemeSwitch by rememberUpdatedState(onThemeSwitch)

    LaunchedEffect(dragState) {
        snapshotFlow { dragState.settledValue }.collect { value ->
            currentOnThemeSwitch(value)
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .clip(CircleShape)
                .drawBehind { drawRect(backgroundColor) }
                .border(1.dp, borderColor, CircleShape)
                .anchoredDraggable(
                    state = dragState,
                    orientation = Orientation.Vertical
                )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_day),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier
                    .size(iconsSize)
                    .padding(8.dp)
                    .clickableNoRipple {
                        coroutineScope.launch { dragState.animateTo(MapTheme.Bright) }
                    }
            )
            Icon(
                painter = painterResource(R.drawable.ic_cloudy),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier
                    .size(iconsSize)
                    .padding(8.dp)
                    .clickableNoRipple {
                        coroutineScope.launch { dragState.animateTo(MapTheme.Default) }
                    }
            )
            Icon(
                painter = painterResource(R.drawable.ic_night),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier
                    .size(iconsSize)
                    .padding(8.dp)
                    .clickableNoRipple {
                        coroutineScope.launch { dragState.animateTo(MapTheme.Dark) }
                    }
            )
        }

        Spacer(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = 0,
                        y = dragState
                            .requireOffset()
                            .roundToInt(),
                    )
                }
                .size(48.dp)
                .background(handleColor, CircleShape)
        )
    }
}