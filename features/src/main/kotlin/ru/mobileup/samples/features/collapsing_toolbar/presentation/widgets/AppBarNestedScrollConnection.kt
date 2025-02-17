package ru.mobileup.samples.features.collapsing_toolbar.presentation.widgets

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

class AppBarNestedScrollConnection : NestedScrollConnection {

    var appBarOffset: Int by mutableIntStateOf(0)
        private set

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val newOffset = (appBarOffset - available.y.toInt()).coerceAtLeast(0)
        appBarOffset = newOffset
        return Offset.Zero
    }
}