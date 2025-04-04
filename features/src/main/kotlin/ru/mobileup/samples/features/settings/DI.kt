package ru.mobileup.samples.features.settings

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.features.settings.presentation.RealSettingsComponent
import ru.mobileup.samples.features.settings.presentation.SettingsComponent

fun ComponentFactory.createSettingsComponent(
    componentContext: ComponentContext,
): SettingsComponent = RealSettingsComponent(componentContext, get(), get())
