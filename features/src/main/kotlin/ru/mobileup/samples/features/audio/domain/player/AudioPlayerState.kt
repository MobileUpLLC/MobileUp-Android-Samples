package ru.mobileup.samples.features.audio.domain.player

import androidx.compose.runtime.Immutable

@Immutable
sealed interface AudioPlayerState {
    data object Idle : AudioPlayerState
    data object Playing : AudioPlayerState
}