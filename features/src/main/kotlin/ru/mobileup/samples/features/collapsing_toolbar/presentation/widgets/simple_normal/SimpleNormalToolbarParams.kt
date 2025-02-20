package ru.mobileup.samples.features.collapsing_toolbar.presentation.widgets.simple_normal

import androidx.compose.ui.util.lerp

data class SimpleNormalToolbarParams(
    val scrollValue: Int,
    val leadingIconWidthPx: Int,
    val density: Float
) {
    private companion object {
        const val COLLAPSED_OFFSET = 1000f
    }

    private val fraction = scrollValue.toFloat().coerceAtMost(COLLAPSED_OFFSET) / COLLAPSED_OFFSET
    val container: Container = Container(fraction)
    val leadingIcon: LeadingIcon = LeadingIcon(fraction)
    val title: Title = Title(fraction, leadingIconWidthPx, density)

    data class Container(val fraction: Float) {
        companion object {
            private const val MAX_CONTAINER_HEIGHT = 96f
            private const val MIN_CONTAINER_HEIGHT = 44f
        }

        val height: Float = lerp(MAX_CONTAINER_HEIGHT, MIN_CONTAINER_HEIGHT, fraction)
    }

    data class LeadingIcon(val fraction: Float) {
        val verticalBias: Float = lerp(-1f, 0f, fraction)
    }

    data class Title(
        val fraction: Float,
        val leadingIconWidthPx: Int,
        val density: Float
    ) {
        private companion object {
            const val TITLE_LEADING_ICON_GAP_DP = 8
            const val TITLE_HOR_PAD_COEFFICIENT = 2f
        }

        val horizontalPadding = lerp(
            start = 0f,
            stop = (leadingIconWidthPx / density) + TITLE_LEADING_ICON_GAP_DP,
            fraction = (fraction * TITLE_HOR_PAD_COEFFICIENT).coerceAtMost(1f)
        )

        val verticalBias = lerp(1f, 0f, fraction)
    }
}