package ru.mobileup.samples.features.collapsing_toolbar.presentation.common.widget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun rememberCustomToolbarState(
    initialHeightOffsetLimit: Float = -Float.MAX_VALUE,
    initialHeightOffset: Float = 0f,
    initialContentOffset: Float = 0f,
) = rememberSaveable(saver = CustomToolbarState.Saver) {
    CustomToolbarState(initialHeightOffsetLimit, initialHeightOffset, initialContentOffset)
}

/**
 * The implementation is fully taken from [androidx.compose.material3.TopAppBarState].
 *
 * A state object that can be hoisted to control and observe the top app bar state. The state is
 * read and updated by a [CustomToolbarScrollBehavior] implementation.
 *
 * In most cases, this state will be created via [CustomToolbarScrollBehavior].
 *
 * @param initialHeightOffsetLimit the initial value for [CustomToolbarState.heightOffsetLimit]
 * @param initialHeightOffset the initial value for [CustomToolbarState.heightOffset]
 * @param initialContentOffset the initial value for [CustomToolbarState.contentOffset]
 */
@Stable
class CustomToolbarState(
    initialHeightOffsetLimit: Float,
    initialHeightOffset: Float,
    initialContentOffset: Float,
) {
    private var _heightOffset = mutableFloatStateOf(initialHeightOffset)

    /**
     * The top app bar's height offset limit in pixels, which represents the limit that a top app
     * bar is allowed to collapse to.
     *
     * Use this limit to coerce the [heightOffset] value when it's updated.
     */
    var heightOffsetLimit by mutableFloatStateOf(initialHeightOffsetLimit)

    /**
     * The top app bar's current height offset in pixels. This height offset is applied to the fixed
     * height of the app bar to control the displayed height when content is being scrolled.
     *
     * Updates to the [heightOffset] value are coerced between zero and [heightOffsetLimit].
     */
    var heightOffset: Float
        get() = _heightOffset.floatValue
        set(newOffset) {
            _heightOffset.floatValue =
                newOffset.coerceIn(minimumValue = heightOffsetLimit, maximumValue = 0f)
        }

    /**
     * The total offset of the content scrolled under the top app bar.
     *
     * This value is updated by a [CustomToolbarScrollBehavior] whenever a nested scroll connection
     * consumes scroll events. A common implementation would update the value to be the sum of all
     * [androidx.compose.ui.input.nestedscroll.NestedScrollConnection.onPostScroll] `consumed.y` values.
     */
    var contentOffset by mutableFloatStateOf(initialContentOffset)

    /**
     * A value that represents the collapsed height percentage of the app bar.
     *
     * A `0.0` represents a fully expanded bar, and `1.0` represents a fully collapsed bar (computed
     * as [heightOffset] / [heightOffsetLimit]).
     */
    val collapsedFraction: Float
        get() = if (heightOffsetLimit != 0f) {
            heightOffset / heightOffsetLimit
        } else {
            0f
        }.coerceIn(0f, 1f)

    companion object {
        val Saver: Saver<CustomToolbarState, *> = listSaver(
            save = { listOf(it.heightOffsetLimit, it.heightOffset, it.contentOffset) },
            restore = {
                CustomToolbarState(
                    initialHeightOffsetLimit = it[0],
                    initialHeightOffset = it[1],
                    initialContentOffset = it[2]
                )
            }
        )
    }
}
