package ru.mobileup.samples.features.multipane_menu.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.panels.ChildPanels
import com.arkivanov.decompose.router.panels.ChildPanelsMode
import com.arkivanov.decompose.router.panels.Panels
import com.arkivanov.decompose.router.panels.PanelsNavigation
import com.arkivanov.decompose.router.panels.activateDetails
import com.arkivanov.decompose.router.panels.childPanels
import com.arkivanov.decompose.router.panels.pop
import com.arkivanov.decompose.router.panels.setMode
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.core.utils.toStateFlow
import ru.mobileup.samples.features.menu.domain.Sample
import ru.mobileup.samples.features.multipane_menu.createSampleDetailsComponent
import ru.mobileup.samples.features.multipane_menu.createSampleListComponent
import ru.mobileup.samples.features.multipane_menu.presentation.details.SampleDetailsComponent
import ru.mobileup.samples.features.multipane_menu.presentation.list.SampleListComponent

@OptIn(ExperimentalDecomposeApi::class)
class RealMultiPaneMenuComponent(
    componentContext: ComponentContext,
    private val onOutput: (MultiPaneMenuComponent.Output) -> Unit,
    private val componentFactory: ComponentFactory,
) : ComponentContext by componentContext, MultiPaneMenuComponent {

    private val navigation = PanelsNavigation<Unit, DetailsConfig, Nothing>()

    @OptIn(ExperimentalSerializationApi::class)
    override val panels: StateFlow<ChildPanels<*, SampleListComponent, *, SampleDetailsComponent, Nothing, Nothing>> =
        childPanels(
            source = navigation,
            serializers = Unit.serializer() to DetailsConfig.serializer(),
            initialPanels = { Panels(main = Unit) },
            handleBackButton = true,
            mainFactory = { _, ctx ->
                componentFactory.createSampleListComponent(ctx, ::onSampleListOutput)
            },
            detailsFactory = { cfg, ctx ->
                componentFactory.createSampleDetailsComponent(
                    ctx,
                    cfg.sample,
                    ::onSampleDetailsOutput
                )
            },
        ).toStateFlow(lifecycle)

    private fun onSampleListOutput(output: SampleListComponent.Output) = when (output) {
        is SampleListComponent.Output.SampleChosen -> navigation.activateDetails(
            DetailsConfig(output.sample)
        )

        SampleListComponent.Output.SettingsRequested -> onOutput(
            MultiPaneMenuComponent.Output.SettingsRequested
        )
    }

    private fun onSampleDetailsOutput(output: SampleDetailsComponent.Output) = when (output) {
        SampleDetailsComponent.Output.OtpSuccessfullyVerified -> navigation.pop()
    }

    override fun onBackClick() = navigation.pop()

    override fun setMode(mode: ChildPanelsMode) = navigation.setMode(mode)

    override fun onSettingsClick() = onOutput(MultiPaneMenuComponent.Output.SettingsRequested)

    @Serializable
    private data class DetailsConfig(val sample: Sample)
}
