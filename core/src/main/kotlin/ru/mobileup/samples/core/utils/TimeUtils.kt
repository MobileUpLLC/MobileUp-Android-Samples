package ru.mobileup.samples.core.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatMillisToMS(millis: Long?) = if (millis != null && (millis < 3600000)) {
    val seconds = millis / 1000
    formatSecondsToMS(seconds)
} else {
    ""
}

fun formatSecondsToMS(seconds: Long): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format(Locale.getDefault(), "%02d:%02d", minutes, remainingSeconds)
}

fun LocalDate.toDay(): String {
    return DateTimeFormatter.ofPattern("dd").format(this)
}

fun timeNow() = Clock.System.now()

fun localDateTimeNow(timeZone: TimeZone = TimeZone.currentSystemDefault()) = timeNow().toLocalDateTime(timeZone)