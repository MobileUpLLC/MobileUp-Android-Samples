package ru.mobileup.samples.features.multipane_menu.presentation.list

import ru.mobileup.samples.features.menu.domain.Sample

interface SampleListComponent {

    fun onButtonClick(sample: Sample)
    fun onSettingsClick()

    sealed interface Output {
        data class SampleChosen(val sample: Sample) : Output
        data object SettingsRequested : Output
    }
}
