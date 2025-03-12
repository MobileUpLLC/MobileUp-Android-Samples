package ru.mobileup.samples.core.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun Dp.toPx(): Int {
    return with(LocalDensity.current) { roundToPx() }
}

@Composable
fun Float.toDp(): Dp {
    return with(LocalDensity.current) { toDp() }
}

@Composable
fun Int.toDp(): Dp {
    return with(LocalDensity.current) { toDp() }
}

fun Modifier.clickableNoRipple(enabled: Boolean = true, listener: () -> Unit) = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = listener,
        enabled = enabled
    )
}