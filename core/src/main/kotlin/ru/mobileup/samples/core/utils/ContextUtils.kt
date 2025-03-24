package ru.mobileup.samples.core.utils

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.provider.Settings
import kotlin.apply
import kotlin.let
import kotlin.runCatching

fun Context.openAppSettings() {
    runCatching {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
            flags = FLAG_ACTIVITY_NEW_TASK
        }.let(::startActivity)
    }
}

fun Context.openLocationSettings() {
    runCatching {
        Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
            flags = FLAG_ACTIVITY_NEW_TASK
        }.let(::startActivity)
    }
}