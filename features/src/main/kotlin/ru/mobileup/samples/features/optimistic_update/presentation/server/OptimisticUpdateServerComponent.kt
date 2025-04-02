package ru.mobileup.samples.features.optimistic_update.presentation.server

import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.utils.LoadableState
import ru.mobileup.samples.features.optimistic_update.domain.PaletteRequest

interface OptimisticUpdateServerComponent {
    val requests: StateFlow<LoadableState<List<PaletteRequest>>>

    fun onAcceptClick(index: Int)
    fun onFailClick(index: Int)
    fun onEndClick(index: Int)

    fun onRefresh()
}