package ru.mobileup.samples.features.audio.data.repository

import kotlinx.coroutines.flow.Flow
import ru.mobileup.samples.features.audio.domain.model.AudioFile
import ru.mobileup.samples.features.audio.domain.model.AudioFileId

interface AudioRecordedFilesRepository {
    val files: Flow<List<AudioFile>>

    suspend fun delete(audioFileId: AudioFileId)
}