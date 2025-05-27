package ru.mobileup.samples.features.audio.domain.recorder

import androidx.compose.runtime.Immutable

@Immutable
sealed interface AudioRecorderState {
    data object Idle : AudioRecorderState
    data object Recording : AudioRecorderState
}