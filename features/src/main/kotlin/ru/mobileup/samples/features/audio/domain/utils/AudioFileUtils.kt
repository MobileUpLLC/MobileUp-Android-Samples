package ru.mobileup.samples.features.audio.domain.utils

import android.content.Context
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import ru.mobileup.samples.core.utils.localDateTimeNow
import java.io.File
import java.time.format.DateTimeFormatter
import java.util.UUID

private const val FILE_FORMAT_SUFFIX = ".aac"

enum class AudioDirectory(
    private val dirName: String
) {
    Recorder("recorder");

    fun toFile(context: Context): File =
        context
            .filesDir
            .resolve("audio_cache/$dirName")
}

fun getAudioFileName(formatSuffix: String = FILE_FORMAT_SUFFIX) =
    UUID.randomUUID().toString() + formatSuffix

fun Context.getOutputAudioFile(formatSuffix: String = FILE_FORMAT_SUFFIX): File {
    val audioRoot = AudioDirectory.Recorder.toFile(this)
    if (!audioRoot.exists()) {
        audioRoot.mkdirs()
    }
    return File(
        audioRoot,
        getAudioFileName(formatSuffix)
    )
}

fun LocalDateTime.displayedTime(): String {
    val now = localDateTimeNow()
    val itsToday = date == now.date
    val itsSameYear = year == now.year

    val pattern = when {
        itsToday -> "HH:mm"
        itsSameYear -> "d MMM"
        else -> "MM.YYYY"
    }
    val formatter = DateTimeFormatter
        .ofPattern(pattern)

    return toJavaLocalDateTime().format(formatter)
}