package ru.mobileup.samples.features.multipane_menu.presentation.list

import com.arkivanov.decompose.ComponentContext
import ru.mobileup.samples.features.menu.domain.Sample

class RealMultiPaneMenuComponent(
    componentContext: ComponentContext,
    private val onOutput: (MultiPaneMenuComponent.Output) -> Unit,
) : ComponentContext by componentContext, MultiPaneMenuComponent {

    override fun onButtonClick(sample: Sample) {
        onOutput(MultiPaneMenuComponent.Output.SampleChosen(sample))
    }

    override fun onSettingsClick() {
        onOutput(MultiPaneMenuComponent.Output.SettingsRequested)
    }
}
