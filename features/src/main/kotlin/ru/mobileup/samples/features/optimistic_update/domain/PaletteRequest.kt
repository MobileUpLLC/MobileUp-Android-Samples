package ru.mobileup.samples.features.optimistic_update.domain

sealed interface PaletteRequest {
    val color: PaletteColor
    val canBeAccepted: Boolean

    data class AddRequest(
        override val color: PaletteColor,
        override val canBeAccepted: Boolean
    ) : PaletteRequest

    data class RemoveRequest(
        override val color: PaletteColor,
        override val canBeAccepted: Boolean
    ) : PaletteRequest
}