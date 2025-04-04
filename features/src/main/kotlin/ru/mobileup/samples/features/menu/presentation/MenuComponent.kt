package ru.mobileup.samples.features.menu.presentation

import ru.mobileup.samples.features.menu.domain.Sample

interface MenuComponent {

    fun onButtonClick(sample: Sample)
    fun onSettingsClick()

    sealed interface Output {
        data class SampleChosen(val sample: Sample) : Output
        data object SettingsRequested : Output
    }
}
