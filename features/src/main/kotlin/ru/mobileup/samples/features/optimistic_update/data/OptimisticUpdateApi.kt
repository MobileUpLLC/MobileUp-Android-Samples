package ru.mobileup.samples.features.optimistic_update.data

interface OptimisticUpdateApi {
    suspend fun getPaletteSize(): Int

    suspend fun getPalette(): List<Long>

    suspend fun addToPalette(color: Long)

    suspend fun removeFromPalette(color: Long)

    suspend fun getAvailableColors(): List<Long>
}