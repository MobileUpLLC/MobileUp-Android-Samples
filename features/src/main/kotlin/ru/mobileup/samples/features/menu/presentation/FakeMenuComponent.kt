package ru.mobileup.samples.features.menu.presentation

import ru.mobileup.samples.features.menu.domain.Sample

class FakeMenuComponent : MenuComponent {
    override fun onButtonClick(sample: Sample) = Unit
    override fun onSettingsClick() = Unit
}
