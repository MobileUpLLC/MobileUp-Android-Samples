package ru.mobileup.samples.features.optimistic_update.data.server

import me.aartikov.replica.single.Replica
import ru.mobileup.samples.features.optimistic_update.domain.PaletteColor
import ru.mobileup.samples.features.optimistic_update.domain.PaletteRequest

interface OptimisticUpdateServerRepository {
    val requestsReplica: Replica<List<PaletteRequest>>
    val paletteReplica: Replica<List<PaletteColor>>

    fun acceptRequestAt(index: Int)
    fun endRequestAt(index: Int)
    fun failRequestAt(index: Int)
}