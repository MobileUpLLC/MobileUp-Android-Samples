package ru.mobileup.samples.features.tutorial.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.utils.toPx

const val TUTORIAL_MESSAGE_HEIGHT_DP = 142f
const val TUTORIAL_MESSAGE_WIDTH_DP = 253f

enum class TutorialMessagePopupPointerAlignment {
    Start, Center, End
}

data class TutorialMessagePopupProperties(
    val alignment: TutorialMessagePopupPointerAlignment,
    val offset: Offset
)

@Composable
fun calculateTutorialMessagePopupProperties(itemRect: Rect): TutorialMessagePopupProperties {
    val screenRect = LocalConfiguration.current.let {
        Rect(
            size = Size(
                width = it.screenWidthDp.dp.toPx().toFloat(),
                height = it.screenHeightDp.dp.toPx().toFloat()
            ),
            offset = Offset(0f, 0f)
        )
    }

    val width = TUTORIAL_MESSAGE_WIDTH_DP.dp
    val height = TUTORIAL_MESSAGE_HEIGHT_DP.dp
    val widthPx = width.toPx()
    val heightPx = height.toPx()
    val size = Size(widthPx.toFloat(), heightPx.toFloat())
    val topPadding = 14.dp
    val topPaddingPx = topPadding.toPx()
    val pointerHorizontalPadding = 28.dp
    val pointerHorizontalPaddingPx = pointerHorizontalPadding.toPx()
    val rectWhenPointerOnStart = itemRect.bottomCenter.plus(
        Offset(
            x = -pointerHorizontalPaddingPx.toFloat(),
            y = +topPaddingPx.toFloat()
        )
    ).let {
        Rect(
            size = size,
            offset = it
        )
    }

    val rectWhenPointerInCenter = itemRect.bottomCenter.plus(
        Offset(
            x = -widthPx.toFloat() / 2,
            y = +topPaddingPx.toFloat()
        )
    ).let {
        Rect(
            size = size,
            offset = it
        )
    }

    val rectWhenPointerOnEnd = itemRect.bottomCenter.plus(
        Offset(
            x = -widthPx.toFloat() + pointerHorizontalPaddingPx,
            y = +topPaddingPx.toFloat()
        )
    ).let {
        Rect(
            size = size,
            offset = it
        )
    }

    val alignment = when {
        rectWhenPointerOnStart in screenRect -> TutorialMessagePopupPointerAlignment.Start
        rectWhenPointerInCenter in screenRect -> TutorialMessagePopupPointerAlignment.Center
        rectWhenPointerOnEnd in screenRect -> TutorialMessagePopupPointerAlignment.End
        else -> TutorialMessagePopupPointerAlignment.Center
    }

    val offset = when (alignment) {
        TutorialMessagePopupPointerAlignment.Start -> rectWhenPointerOnStart.topLeft
        TutorialMessagePopupPointerAlignment.Center -> rectWhenPointerInCenter.topLeft
        TutorialMessagePopupPointerAlignment.End -> rectWhenPointerOnEnd.topLeft
    }

    return TutorialMessagePopupProperties(
        alignment = alignment,
        offset = offset
    )
}

private operator fun Rect.contains(other: Rect): Boolean {
    return this.left <= other.left && this.right >= other.right &&
            this.top <= other.top && this.bottom >= other.bottom
}
