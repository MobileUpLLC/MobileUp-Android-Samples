package ru.mobileup.samples.features.audio.domain.model

import android.net.Uri
import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDateTime
import ru.mobileup.samples.core.utils.localDateTimeNow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@JvmInline
value class AudioFileId(val value: String)

@Immutable
data class AudioFile(
    val id: AudioFileId,
    val duration: Long,
    val uri: Uri,
    val lastModified: LocalDateTime
) {
    companion object {
        @OptIn(ExperimentalUuidApi::class)
        val MOCKS: List<AudioFile>
            get() = listOf(
                AudioFile(
                    id = AudioFileId(Uuid.random().toString()),
                    duration = 0,
                    uri = Uri.EMPTY,
                    lastModified = localDateTimeNow()
                ),
                AudioFile(
                    id = AudioFileId(Uuid.random().toString()),
                    duration = 100000,
                    uri = Uri.EMPTY,
                    lastModified = localDateTimeNow()
                ),
                AudioFile(
                    id = AudioFileId(Uuid.random().toString()),
                    duration = 100000,
                    uri = Uri.EMPTY,
                    lastModified = localDateTimeNow()
                ),
                AudioFile(
                    id = AudioFileId(Uuid.random().toString()),
                    duration = 100000,
                    uri = Uri.EMPTY,
                    lastModified = localDateTimeNow()
                ),
                AudioFile(
                    id = AudioFileId(Uuid.random().toString()),
                    duration = 100000,
                    uri = Uri.EMPTY,
                    lastModified = localDateTimeNow()
                ),
            )
    }
}