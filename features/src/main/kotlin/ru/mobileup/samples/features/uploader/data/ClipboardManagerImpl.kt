package ru.mobileup.samples.features.uploader.data

import android.content.ClipData
import android.content.Context

class ClipboardManagerImpl(
    private val context: Context
) : ClipboardManager {

    override fun copyToClipboard(string: String) {
        val manager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        manager.setPrimaryClip(
            ClipData.newPlainText("link", string)
        )
    }
}