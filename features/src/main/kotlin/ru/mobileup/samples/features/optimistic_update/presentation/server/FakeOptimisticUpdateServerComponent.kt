package ru.mobileup.samples.features.optimistic_update.presentation.server

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.utils.LoadableState
import ru.mobileup.samples.features.optimistic_update.domain.PaletteColor
import ru.mobileup.samples.features.optimistic_update.domain.PaletteRequest

class FakeOptimisticUpdateServerComponent : OptimisticUpdateServerComponent {
    override val requests: StateFlow<LoadableState<List<PaletteRequest>>> =
        MutableStateFlow(
            LoadableState(
                data = listOf(
                    PaletteRequest.AddRequest(
                        color = PaletteColor(0xFFFF00FF),
                        canBeAccepted = true
                    ),
                    PaletteRequest.RemoveRequest(
                        color = PaletteColor(0xFF0000FF),
                        canBeAccepted = false
                    )
                )
            )
        )

    override fun onAcceptClick(index: Int): Unit = Unit

    override fun onFailClick(index: Int): Unit = Unit

    override fun onEndClick(index: Int): Unit = Unit

    override fun onRefresh(): Unit = Unit
}
