package ru.mobileup.samples.features.multipane_menu

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.features.menu.domain.Sample
import ru.mobileup.samples.features.multipane_menu.presentation.MultiPaneMenuComponent
import ru.mobileup.samples.features.multipane_menu.presentation.RealMultiPaneMenuComponent
import ru.mobileup.samples.features.multipane_menu.presentation.details.RealSampleDetailsComponent
import ru.mobileup.samples.features.multipane_menu.presentation.details.SampleDetailsComponent
import ru.mobileup.samples.features.multipane_menu.presentation.list.RealSampleListComponent
import ru.mobileup.samples.features.multipane_menu.presentation.list.SampleListComponent

fun ComponentFactory.createMultiPaneComponent(
    componentContext: ComponentContext,
    onOutput: (MultiPaneMenuComponent.Output) -> Unit,
): MultiPaneMenuComponent = RealMultiPaneMenuComponent(componentContext, onOutput, get())

fun ComponentFactory.createSampleListComponent(
    componentContext: ComponentContext,
    onOutput: (SampleListComponent.Output) -> Unit,
): SampleListComponent = RealSampleListComponent(componentContext, onOutput)

fun ComponentFactory.createSampleDetailsComponent(
    componentContext: ComponentContext,
    sample: Sample,
    onOutput: (SampleDetailsComponent.Output) -> Unit,
): SampleDetailsComponent = RealSampleDetailsComponent(componentContext, sample, onOutput, get())
