package ru.mobileup.samples.features.audio.data.repository

import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.METADATA_KEY_DURATION
import android.net.Uri
import android.os.Build
import android.os.FileObserver
import android.os.FileObserver.ATTRIB
import android.os.FileObserver.CLOSE_WRITE
import android.os.FileObserver.DELETE
import android.os.FileObserver.MOVED_FROM
import android.os.FileObserver.MOVED_TO
import androidx.core.net.toFile
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ru.mobileup.samples.core.utils.localDateTimeNow
import ru.mobileup.samples.features.audio.domain.model.AudioFile
import ru.mobileup.samples.features.audio.domain.model.AudioFileId
import ru.mobileup.samples.features.audio.domain.utils.AudioDirectory
import ru.mobileup.samples.features.audio.domain.utils.AudioFormat

class AudioRecordedFilesRepositoryImpl(
    private val context: Context,
) : AudioRecordedFilesRepository {

    private val observerMask = DELETE or MOVED_FROM or MOVED_TO or CLOSE_WRITE or ATTRIB

    private val audioRecorderFolder = AudioDirectory.Recorder.toFile(context)

    override val files: Flow<List<AudioFile>> = callbackFlow {

        fun emitCurrentFiles() {
            val files = audioRecorderFolder
                .listFiles { file ->
                    file.extension.equals(AudioFormat.FORMAT, ignoreCase = true)
                }
                ?.toList()
                ?.mapNotNull { file ->
                    val fileUri = file.toUri()
                    val duration = getFileDuration(fileUri)
                    if (duration == 0L) {
                        null
                    } else {
                        AudioFile(
                            id = AudioFileId(fileUri.toString()),
                            duration = getFileDuration(fileUri),
                            uri = fileUri,
                            lastModified = getFileLocalDateTime(fileUri)
                        )
                    }
                }
                ?.sortedByDescending { it.lastModified }
                ?: emptyList()
            trySend(files)
        }

        val observer = fileObserver { emitCurrentFiles() }

        observer.startWatching()

        emitCurrentFiles()

        awaitClose { observer.stopWatching() }
    }.flowOn(Dispatchers.IO)

    override suspend fun delete(audioFileId: AudioFileId) {
        withContext(Dispatchers.IO) {
            audioFileId.value
                .toUri()
                .toFile()
                .deleteRecursively()
        }
    }

    private fun getFileDuration(uri: Uri): Long {
        try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(context, uri)
            return (retriever.extractMetadata(METADATA_KEY_DURATION))?.toLong() ?: 0L
        } catch (e: Exception) {
            return 0L
        }
    }

    private fun getFileLocalDateTime(uri: Uri): LocalDateTime {
        try {
            val file = uri.toFile()
            val lastModified = file.lastModified()
            return Instant.fromEpochMilliseconds(lastModified).toLocalDateTime(TimeZone.currentSystemDefault())
        } catch (e: Exception) {
            return localDateTimeNow()
        }
    }

    private fun fileObserver(
        onFilesUpdated: () -> Unit
    ): FileObserver {
        val onEventObserver = { _: Int, _: String? ->
            onFilesUpdated()
        }
        if (!audioRecorderFolder.exists()) {
            audioRecorderFolder.mkdirs()
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            object : FileObserver(audioRecorderFolder, observerMask) {
                override fun onEvent(p0: Int, p1: String?) {
                    onEventObserver(p0, p1)
                }
            }
        } else {
            @Suppress("DEPRECATION")
            object : FileObserver(audioRecorderFolder.absolutePath, observerMask) {
                override fun onEvent(p0: Int, p1: String?) {
                    onEventObserver(p0, p1)
                }
            }
        }
    }
}