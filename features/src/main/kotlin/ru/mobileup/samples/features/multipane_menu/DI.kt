package ru.mobileup.samples.features.multipane_menu

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.features.menu.domain.Sample
import ru.mobileup.samples.features.multipane_menu.presentation.MultiPaneComponent
import ru.mobileup.samples.features.multipane_menu.presentation.RealMultiPaneComponent
import ru.mobileup.samples.features.multipane_menu.presentation.details.MultiPaneDetailsComponent
import ru.mobileup.samples.features.multipane_menu.presentation.details.RealMultiPaneDetailsComponent
import ru.mobileup.samples.features.multipane_menu.presentation.list.MultiPaneMenuComponent
import ru.mobileup.samples.features.multipane_menu.presentation.list.RealMultiPaneMenuComponent

fun ComponentFactory.createMultiPaneComponent(
    componentContext: ComponentContext,
    onOutput: (MultiPaneComponent.Output) -> Unit,
): MultiPaneComponent = RealMultiPaneComponent(componentContext, onOutput, get())

fun ComponentFactory.createMultiPaneMenuComponent(
    componentContext: ComponentContext,
    onOutput: (MultiPaneMenuComponent.Output) -> Unit,
): MultiPaneMenuComponent = RealMultiPaneMenuComponent(componentContext, onOutput)

fun ComponentFactory.createMultiPaneDetailsComponent(
    componentContext: ComponentContext,
    sample: Sample,
    onOutput: (MultiPaneDetailsComponent.Output) -> Unit,
): MultiPaneDetailsComponent = RealMultiPaneDetailsComponent(componentContext, sample, onOutput, get())
