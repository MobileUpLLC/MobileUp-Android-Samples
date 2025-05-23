package ru.mobileup.samples.features.multipane_menu.presentation

import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.panels.ChildPanels
import com.arkivanov.decompose.router.panels.ChildPanelsMode
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.utils.PredictiveBackComponent
import ru.mobileup.samples.features.multipane_menu.presentation.details.MultiPaneDetailsComponent
import ru.mobileup.samples.features.multipane_menu.presentation.list.MultiPaneMenuComponent

@OptIn(ExperimentalDecomposeApi::class)
interface MultiPaneComponent : PredictiveBackComponent {

    val panels: StateFlow<ChildPanels<*, MultiPaneMenuComponent, *, MultiPaneDetailsComponent, Nothing, Nothing>>

    fun setMode(mode: ChildPanelsMode)
    fun onSettingsClick()

    sealed interface Output {
        object SettingsRequested : Output
    }
}
