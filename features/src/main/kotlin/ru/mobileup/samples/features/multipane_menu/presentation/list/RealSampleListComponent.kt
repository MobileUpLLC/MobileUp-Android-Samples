package ru.mobileup.samples.features.multipane_menu.presentation.list

import com.arkivanov.decompose.ComponentContext
import ru.mobileup.samples.features.menu.domain.Sample

class RealSampleListComponent(
    componentContext: ComponentContext,
    private val onOutput: (SampleListComponent.Output) -> Unit,
) : ComponentContext by componentContext, SampleListComponent {

    override fun onButtonClick(sample: Sample) {
        onOutput(SampleListComponent.Output.SampleChosen(sample))
    }

    override fun onSettingsClick() {
        onOutput(SampleListComponent.Output.SettingsRequested)
    }
}
