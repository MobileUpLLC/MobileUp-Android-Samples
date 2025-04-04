package ru.mobileup.samples.features.menu.presentation

import com.arkivanov.decompose.ComponentContext
import ru.mobileup.samples.features.menu.domain.Sample

class RealMenuComponent(
    componentContext: ComponentContext,
    private val onOutput: (MenuComponent.Output) -> Unit
) : ComponentContext by componentContext, MenuComponent {

    override fun onButtonClick(sample: Sample) {
        onOutput(MenuComponent.Output.SampleChosen(sample))
    }

    override fun onSettingsClick() {
        onOutput(MenuComponent.Output.SettingsRequested)
    }
}
