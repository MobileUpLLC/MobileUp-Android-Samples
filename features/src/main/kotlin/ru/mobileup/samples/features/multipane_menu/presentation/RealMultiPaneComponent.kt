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
import ru.mobileup.samples.features.multipane_menu.createMultiPaneDetailsComponent
import ru.mobileup.samples.features.multipane_menu.createMultiPaneMenuComponent
import ru.mobileup.samples.features.multipane_menu.presentation.details.MultiPaneDetailsComponent
import ru.mobileup.samples.features.multipane_menu.presentation.list.MultiPaneMenuComponent

@OptIn(ExperimentalDecomposeApi::class)
class RealMultiPaneComponent(
    componentContext: ComponentContext,
    private val onOutput: (MultiPaneComponent.Output) -> Unit,
    private val componentFactory: ComponentFactory,
) : ComponentContext by componentContext, MultiPaneComponent {

    private val navigation = PanelsNavigation<Unit, DetailsConfig, Nothing>()

    @OptIn(ExperimentalSerializationApi::class)
    override val panels: StateFlow<ChildPanels<*, MultiPaneMenuComponent, *, MultiPaneDetailsComponent, Nothing, Nothing>> =
        childPanels(
            source = navigation,
            serializers = Unit.serializer() to DetailsConfig.serializer(),
            initialPanels = { Panels(main = Unit) },
            handleBackButton = true,
            mainFactory = { _, ctx ->
                componentFactory.createMultiPaneMenuComponent(ctx, ::onMenuOutput)
            },
            detailsFactory = { cfg, ctx ->
                componentFactory.createMultiPaneDetailsComponent(
                    ctx,
                    cfg.sample,
                    ::onDetailsOutput
                )
            },
        ).toStateFlow(lifecycle)

    private fun onMenuOutput(output: MultiPaneMenuComponent.Output) = when (output) {
        is MultiPaneMenuComponent.Output.SampleChosen -> navigation.activateDetails(
            DetailsConfig(output.sample)
        )

        MultiPaneMenuComponent.Output.SettingsRequested -> onOutput(
            MultiPaneComponent.Output.SettingsRequested
        )
    }

    private fun onDetailsOutput(output: MultiPaneDetailsComponent.Output) = when (output) {
        MultiPaneDetailsComponent.Output.OtpSuccessfullyVerified -> navigation.pop()
    }

    override fun onBackClick() = navigation.pop()

    override fun setMode(mode: ChildPanelsMode) = navigation.setMode(mode)

    override fun onSettingsClick() = onOutput(MultiPaneComponent.Output.SettingsRequested)

    @Serializable
    private data class DetailsConfig(val sample: Sample)
}
