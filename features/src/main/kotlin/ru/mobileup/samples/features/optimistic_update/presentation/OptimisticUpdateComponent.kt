package ru.mobileup.samples.features.optimistic_update.presentation

import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.dialog.DialogControl
import ru.mobileup.samples.core.utils.LoadableState
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.optimistic_update.domain.PaletteColor
import ru.mobileup.samples.features.optimistic_update.presentation.server.OptimisticUpdateServerComponent

interface OptimisticUpdateComponent {

    val state: StateFlow<LoadableState<Model>>
    val serverDialog: DialogControl<*, OptimisticUpdateServerComponent>

    fun onAddColorClick(color: PaletteColor)
    fun onRemoveColorClick(color: PaletteColor)
    fun onTabClick(tab: Tab)
    fun onServerShowClick()
    fun onRefresh()

    enum class Tab(val resId: Int) {
        AllColors(R.string.optimistic_update_client_all_colors_tab),
        Palette(R.string.optimistic_update_client_palette_tab)
    }

    data class Model(
        val paletteSize: Int,
        val palette: List<PaletteColor>,
        val allColors: List<PaletteColor>,
        val selectedTab: Tab
    )
}