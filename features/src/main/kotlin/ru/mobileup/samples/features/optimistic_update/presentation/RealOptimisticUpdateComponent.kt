package ru.mobileup.samples.features.optimistic_update.presentation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import me.aartikov.replica.algebra.normal.combine
import me.aartikov.replica.algebra.normal.flowReplica
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.core.dialog.dialogControl
import ru.mobileup.samples.core.error_handling.ErrorHandler
import ru.mobileup.samples.core.utils.observe
import ru.mobileup.samples.features.optimistic_update.createOptimisticUpdateServerComponent
import ru.mobileup.samples.features.optimistic_update.data.client.OptimisticUpdateClientRepository
import ru.mobileup.samples.features.optimistic_update.domain.PaletteColor
import ru.mobileup.samples.features.optimistic_update.domain.job_launcher.JobLauncherStore
import ru.mobileup.samples.features.optimistic_update.presentation.server.OptimisticUpdateServerComponent

class RealOptimisticUpdateComponent(
    componentContext: ComponentContext,
    private val clientRepository: OptimisticUpdateClientRepository,
    private val errorHandler: ErrorHandler,
    private val componentFactory: ComponentFactory,
    private val jobLauncherStore: JobLauncherStore
) : ComponentContext by componentContext, OptimisticUpdateComponent {

    private val paletteJobLauncher =
        jobLauncherStore.getOrCreateEnqueueingLauncher<PaletteColor, Boolean>()

    override val serverDialog = dialogControl<Unit, OptimisticUpdateServerComponent>(
        key = "serverDialog",
        dialogComponentFactory = { _, ctx, _ ->
            componentFactory.createOptimisticUpdateServerComponent(ctx)
        }
    )

    override fun onServerShowClick() = serverDialog.show(Unit)

    private val selectedTab = MutableStateFlow(OptimisticUpdateComponent.Tab.AllColors)

    private val modelReplica = combine(
        clientRepository.paletteColorsCountReplica,
        clientRepository.paletteColorsReplica,
        clientRepository.availableColorsReplica,
        flowReplica(selectedTab),
        OptimisticUpdateComponent::Model
    )

    override val state = modelReplica.observe(this, errorHandler)

    override fun onAddColorClick(color: PaletteColor) {
        paletteJobLauncher.launchJob(
            key = color,
            targetState = true,
        ) {
            clientRepository.addColor(color)
        }
    }

    override fun onRemoveColorClick(color: PaletteColor) {
        paletteJobLauncher.launchJob(
            key = color,
            targetState = false,
        ) {
            clientRepository.removeColor(color)
        }
    }

    override fun onTabClick(tab: OptimisticUpdateComponent.Tab) = selectedTab.update { tab }

    override fun onRefresh() = modelReplica.refresh()
}