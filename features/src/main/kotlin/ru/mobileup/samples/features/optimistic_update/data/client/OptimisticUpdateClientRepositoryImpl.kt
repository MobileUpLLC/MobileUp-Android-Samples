package ru.mobileup.samples.features.optimistic_update.data.client

import me.aartikov.replica.client.ReplicaClient
import me.aartikov.replica.single.ReplicaSettings
import ru.mobileup.samples.features.optimistic_update.data.DoOnAction
import ru.mobileup.samples.features.optimistic_update.data.OptimisticUpdateApi
import ru.mobileup.samples.features.optimistic_update.data.ReplicaActions
import ru.mobileup.samples.features.optimistic_update.data.checkIsOptimistic
import ru.mobileup.samples.features.optimistic_update.data.sendOptimisticAction
import ru.mobileup.samples.features.optimistic_update.domain.PaletteColor
import kotlin.time.Duration.Companion.minutes

class OptimisticUpdateClientRepositoryImpl(
    private val replicaClient: ReplicaClient,
    private val api: OptimisticUpdateApi
) : OptimisticUpdateClientRepository {
    override val paletteColorsCountReplica = replicaClient.createReplica(
        name = "paletteColorsCount",
        settings = ReplicaSettings(3.minutes),
        behaviours = listOf(
            DoOnAction(
                mapNotNull = {
                    it.checkIsOptimistic<ReplicaActions.PaletteUpdate>()
                },
                handler = { action ->
                    action.applyOptimisticUpdate(this) {
                        when (action.sourceAction) {
                            is ReplicaActions.PaletteUpdate.Add -> it + 1
                            is ReplicaActions.PaletteUpdate.Remove -> it - 1
                        }
                    }
                }
            )
        ),
        fetcher = {
            api.getPaletteSize()
        }
    )

    override val paletteColorsReplica = replicaClient.createReplica(
        name = "paletteColors",
        settings = ReplicaSettings(3.minutes),
        behaviours = listOf(
            DoOnAction(
                mapNotNull = {
                    it.checkIsOptimistic<ReplicaActions.PaletteUpdate>()
                },
                handler = { action ->
                    action.applyOptimisticUpdate(this) { list ->
                        when (val sourceAction = action.sourceAction) {
                            is ReplicaActions.PaletteUpdate.Add -> list + sourceAction.color
                            is ReplicaActions.PaletteUpdate.Remove -> {
                                list.filter {
                                    it != sourceAction.color
                                }
                            }
                        }
                    }
                }
            )
        ),
        fetcher = {
            api.getPalette().map(::PaletteColor)
        }
    )

    override val availableColorsReplica = replicaClient.createReplica(
        name = "allColorsReplica",
        settings = ReplicaSettings(3.minutes),
        behaviours = listOf(
            DoOnAction(
                mapNotNull = {
                    it.checkIsOptimistic<ReplicaActions.PaletteUpdate>()
                },
                handler = { action ->
                    action.applyOptimisticUpdate(this) {
                        when (val sourceAction = action.sourceAction) {
                            is ReplicaActions.PaletteUpdate.Add -> it - sourceAction.color
                            is ReplicaActions.PaletteUpdate.Remove -> it + sourceAction.color
                        }
                    }
                }
            )
        ),
        fetcher = {
            api.getAvailableColors().map(::PaletteColor)
        }
    )

    override suspend fun addColor(color: PaletteColor) {
        replicaClient.sendOptimisticAction(
            action = ReplicaActions.PaletteUpdate.Add(color),
            block = {
                api.addToPalette(color.value)
            }
        )
    }

    override suspend fun removeColor(color: PaletteColor) {
        replicaClient.sendOptimisticAction(
            action = ReplicaActions.PaletteUpdate.Remove(color),
            block = {
                api.removeFromPalette(color.value)
            }
        )
    }
}