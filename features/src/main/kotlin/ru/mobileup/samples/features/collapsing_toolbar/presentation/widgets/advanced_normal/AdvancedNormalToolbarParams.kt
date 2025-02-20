package ru.mobileup.samples.features.collapsing_toolbar.presentation.widgets.advanced_normal

import androidx.compose.ui.util.lerp

data class AdvancedNormalToolbarParams(
    val scrollValue: Int,
) {
    private companion object {
        const val COLLAPSED_OFFSET = 2000f
    }

    private val fraction = scrollValue.toFloat().coerceAtMost(COLLAPSED_OFFSET) / COLLAPSED_OFFSET
    val container: Container = Container(fraction)
    val background: Background = Background(fraction)
    val playerInfo: PlayerPhoto = PlayerPhoto(fraction)

    class Container(
        fraction: Float
    ) {
        private companion object {
            const val MAX_HEIGHT = 366f
            const val MIN_HEIGHT = 184f
        }
        val height = lerp(MAX_HEIGHT, MIN_HEIGHT, fraction)
    }

    class Background(
        fraction: Float
    ) {
        val alpha = lerp(1f, 0f, fraction)
    }

    class PlayerPhoto(
        fraction: Float
    ) {
        val photoWidth = lerp(MAX_WIDTH, MIN_SIZE, fraction)
        val photoHeight = lerp(MAX_HEIGHT, MIN_SIZE, fraction)
        val horizontalBias = lerp(0f, -BIAS, fraction)
        val verticalBias =
            lerp(
                start = 1f,
                stop = BIAS,
                fraction.takeIf { it > VERTICAL_BIAS_START_FRACTION } ?: 0f
            )
        val showCollapsedElements: Boolean = fraction == 1f
        val playerNumberVerticalBias = BIAS

        private companion object {
            const val BIAS = 0.85f
            const val MIN_SIZE = 40f
            const val MAX_WIDTH = 329f
            const val MAX_HEIGHT = 288f
            const val VERTICAL_BIAS_START_FRACTION = 0.85f
        }
    }
}