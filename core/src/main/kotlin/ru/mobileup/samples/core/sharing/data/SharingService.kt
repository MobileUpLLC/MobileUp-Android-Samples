package ru.mobileup.samples.core.sharing.data

import android.net.Uri

interface SharingService {
    fun shareMedia(uri: Uri, mimeType: String)
    fun shareText(text: String)
}