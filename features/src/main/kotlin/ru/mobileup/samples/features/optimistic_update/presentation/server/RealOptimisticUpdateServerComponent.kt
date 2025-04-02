package ru.mobileup.samples.features.optimistic_update.presentation.server

import com.arkivanov.decompose.ComponentContext
import ru.mobileup.samples.core.error_handling.ErrorHandler
import ru.mobileup.samples.core.utils.observe
import ru.mobileup.samples.features.optimistic_update.data.server.OptimisticUpdateServerRepository

class RealOptimisticUpdateServerComponent(
    componentContext: ComponentContext,
    private val serverRepository: OptimisticUpdateServerRepository,
    errorHandler: ErrorHandler
) : ComponentContext by componentContext, OptimisticUpdateServerComponent {

    private val stateReplica = serverRepository.requestsReplica

    override val requests = stateReplica.observe(this, errorHandler)

    override fun onAcceptClick(index: Int) {
        serverRepository.acceptRequestAt(index)
    }

    override fun onFailClick(index: Int) {
        serverRepository.failRequestAt(index)
    }

    override fun onEndClick(index: Int) {
        serverRepository.endRequestAt(index)
    }

    override fun onRefresh() = stateReplica.refresh()
}