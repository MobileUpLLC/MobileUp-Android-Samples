package ru.mobileup.samples.core.utils

import java.util.Locale

fun formatMillisToMS(millis: Long?) = if (millis != null && (millis < 3600000)) {
    val seconds = millis / 1000
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    String.format(Locale.getDefault(), "%02d:%02d", minutes, remainingSeconds)
} else {
    ""
}