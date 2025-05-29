package ru.mobileup.samples.features.audio.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class PlayingAudioFile(
    val id: AudioFileId,
    val playedDuration: Long,
    val isPlaying: Boolean
)