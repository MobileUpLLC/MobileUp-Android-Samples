package ru.mobileup.samples.features.collapsing_toolbar.presentation.widgets.common_multiline

import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.util.lerp

data class CollapsingToolbarMultilineParams(
    private val scrollValue: Int,
    private val leadingIconSize: IntSize,
    private val trailingIconsSize: IntSize,
    private val collapsedTitleSize: IntSize,
    private val expandedTitleSize: IntSize,
    private val maxContainerHeight: Int,
    private val density: Float
) {
    private companion object {
        const val COLLAPSED_OFFSET = 1000f
        const val MIN_CONTAINER_HEIGHT = 56f

        const val EXPANDED_FONT_SIZE = 32f
        const val FONT_SIZE_SCALE_COEFFICIENT = 0.5f

        const val EXPANDED_TITLE_TOP_PADDING = 16
    }

    private val fraction = scrollValue.toFloat().coerceAtMost(COLLAPSED_OFFSET) / COLLAPSED_OFFSET
    private val maxHeight = maxOf(
        leadingIconSize.height, trailingIconsSize.height, collapsedTitleSize.height
    ).toFloat()
    private val leadingIconTopPadding = calculateIconTopPadding(fraction, leadingIconSize.height)
    private val trailingIconsTopPadding = calculateIconTopPadding(fraction, trailingIconsSize.height)
    private fun calculateIconTopPadding(fraction: Float, height: Int): Float {
        val minPadding = (MIN_CONTAINER_HEIGHT - (height / density)) / 2
        val result = if (fraction == 1f) {
            maxOf((maxHeight - height) / 2 / density, minPadding)
        } else {
            minPadding
        }

        return result.coerceAtLeast(0f)
    }
    private val maxIconsTopPadding = maxOf(leadingIconTopPadding, trailingIconsTopPadding)

    val container: Container = Container(
        fraction = fraction,
        maxContainerHeight = maxContainerHeight,
        maxIconsTopPadding = maxIconsTopPadding,
        density = density
    )
    val icons: Icons = Icons(
        fraction = fraction,
        leadingIconSize = leadingIconSize,
        trailingIconsSize = trailingIconsSize,
        maxHeight = maxHeight,
        leadingIconTopPadding = leadingIconTopPadding,
        trailingIconsTopPadding = trailingIconsTopPadding,
        density = density
    )
    val expandedTitle: ExpandedTitle = ExpandedTitle(
        fraction = fraction,
        leadingIconSize = leadingIconSize,
        trailingIconsSize = trailingIconsSize,
        titleSize = expandedTitleSize,
        maxHeight = maxHeight,
        maxIconsTopPadding = maxIconsTopPadding,
        density = density
    )

    val collapsedTitle: CollapsedTitle = CollapsedTitle(
        fraction = fraction,
        leadingIconSize = leadingIconSize,
        trailingIconsSize = trailingIconsSize,
        titleSize = collapsedTitleSize,
        maxHeight = maxHeight,
        maxIconsTopPadding = maxIconsTopPadding,
        density = density
    )

    data class Container(
        private val fraction: Float,
        private val maxContainerHeight: Int,
        private val maxIconsTopPadding: Float,
        private val density: Float,
    ) {
        val minHeight: Float = MIN_CONTAINER_HEIGHT
        val maxHeight: Float = lerp(
            start = maxContainerHeight.div(density).plus(maxIconsTopPadding),
            stop = minHeight,
            fraction = fraction
        )
            .coerceAtLeast(MIN_CONTAINER_HEIGHT)
    }

    data class Icons(
        val leadingIconTopPadding: Float,
        val trailingIconsTopPadding: Float,
        private val fraction: Float,
        private val trailingIconsSize: IntSize,
        private val maxHeight: Float,
        private val density: Float,
        private val leadingIconSize: IntSize
    ) {
        val maxSize = MIN_CONTAINER_HEIGHT
    }

    data class ExpandedTitle(
        private val fraction: Float,
        private val leadingIconSize: IntSize,
        private val trailingIconsSize: IntSize,
        private val titleSize: IntSize,
        private val maxHeight: Float,
        private val maxIconsTopPadding: Float,
        private val density: Float
    ) {

        val scale = lerp(1f, FONT_SIZE_SCALE_COEFFICIENT, fraction)
        val fontSize = EXPANDED_FONT_SIZE

        private val maxIconHeight = maxOf(leadingIconSize.height, trailingIconsSize.height).toFloat()
        val expandedTopOffset = maxIconHeight + (EXPANDED_TITLE_TOP_PADDING * density)

        val alpha = lerp(1f, 0f, fraction)
    }

    data class CollapsedTitle(
        private val fraction: Float,
        private val leadingIconSize: IntSize,
        private val trailingIconsSize: IntSize,
        private val titleSize: IntSize,
        private val maxHeight: Float,
        private val maxIconsTopPadding: Float,
        private val density: Float
    ) {
        private companion object {
            const val TITLE_LEADING_ICON_GAP_DP = 8
        }

        val fontSize = EXPANDED_FONT_SIZE * FONT_SIZE_SCALE_COEFFICIENT

        val startPadding = leadingIconSize.width / density + TITLE_LEADING_ICON_GAP_DP

        val endPadding = trailingIconsSize.width / density

        val topPadding = calculateTopPadding(titleSize.height)

        private fun calculateTopPadding(height: Int): Float {
            val minPadding = (MIN_CONTAINER_HEIGHT - (height / density)) / 2
            val result = if (fraction == 1f) {
                maxOf((maxHeight - height) / 2 / density, minPadding)
            } else {
                minPadding
            }

            return result.coerceAtLeast(0f)
        }

        val alpha = lerp(0f, 1f, fraction)
    }
}