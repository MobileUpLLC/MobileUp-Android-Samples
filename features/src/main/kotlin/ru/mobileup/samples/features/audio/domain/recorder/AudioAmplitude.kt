package ru.mobileup.samples.features.audio.domain.recorder

import androidx.annotation.FloatRange
import androidx.compose.runtime.Immutable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Immutable
data class AudioAmplitude(
    val id: String = Uuid.random().toString(),
    @FloatRange(0.0, 1.0) val value: Float
) {
    companion object {
        val zero = AudioAmplitude(
            id = Uuid.random().toString(),
            value = 0f
        )
    }
}