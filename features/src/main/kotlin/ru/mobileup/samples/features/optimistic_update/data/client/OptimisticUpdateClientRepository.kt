package ru.mobileup.samples.features.optimistic_update.data.client

import me.aartikov.replica.single.Replica
import ru.mobileup.samples.features.optimistic_update.domain.PaletteColor

interface OptimisticUpdateClientRepository {
    val paletteColorsCountReplica: Replica<Int>
    val paletteColorsReplica: Replica<List<PaletteColor>>
    val availableColorsReplica: Replica<List<PaletteColor>>

    suspend fun addColor(color: PaletteColor)
    suspend fun removeColor(color: PaletteColor)
}