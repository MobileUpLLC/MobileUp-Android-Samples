package ru.mobileup.samples.features.collapsing_toolbar.presentation.widgets.common

import androidx.compose.ui.util.lerp

data class CollapsingToolbarCommonParams(
    val scrollValue: Int,
    val leadingIconWidthPx: Int,
    val trailingIconsWidthPx: Int,
    val density: Float
) {
    private companion object {
        const val COLLAPSED_OFFSET = 1000f
    }

    private val fraction = scrollValue.toFloat().coerceAtMost(COLLAPSED_OFFSET) / COLLAPSED_OFFSET
    val container: Container = Container(fraction)
    val icons: Icons = Icons(fraction)
    val title: Title = Title(fraction, leadingIconWidthPx, trailingIconsWidthPx, density)

    data class Container(val fraction: Float) {
        private companion object {
            const val MAX_CONTAINER_HEIGHT = 96f
            const val MIN_CONTAINER_HEIGHT = 44f
        }

        val height: Float = lerp(MAX_CONTAINER_HEIGHT, MIN_CONTAINER_HEIGHT, fraction)
    }

    data class Icons(val fraction: Float) {

        private companion object {
            const val MAX_SIZE = 32
        }

        val verticalBias: Float = lerp(-1f, 0f, fraction)

        val maxSize = MAX_SIZE
    }

    data class Title(
        val fraction: Float,
        val leadingIconWidthPx: Int,
        val trailingIconsWidthPx: Int,
        val density: Float
    ) {
        private companion object {
            const val TITLE_LEADING_ICON_GAP_DP = 8
            const val TITLE_PAD_COEFFICIENT = 2f
            const val EXPANDED_SIZE = 36f
            const val COLLAPSED_SIZE = 24f
        }

        val size = lerp(EXPANDED_SIZE, COLLAPSED_SIZE, fraction)

        private val paddingFraction = (fraction * TITLE_PAD_COEFFICIENT).coerceAtMost(1f)

        val startPadding = lerp(
            start = 0f,
            stop = (leadingIconWidthPx / density) + TITLE_LEADING_ICON_GAP_DP,
            fraction = paddingFraction
        ).coerceAtLeast(0f)

        val endPadding = lerp(
            start = 0f,
            stop = (trailingIconsWidthPx / density),
            fraction = paddingFraction
        ).coerceAtLeast(0f)

        val verticalBias = lerp(1f, 0f, fraction)
    }
}