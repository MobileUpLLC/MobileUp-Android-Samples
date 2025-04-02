package ru.mobileup.samples.features.optimistic_update.data

import ru.mobileup.samples.features.optimistic_update.domain.PaletteColor

object ReplicaActions {

    sealed interface PaletteUpdate : ReplicaAction {
        data class Add(val color: PaletteColor) : PaletteUpdate
        data class Remove(val color: PaletteColor) : PaletteUpdate
    }
}