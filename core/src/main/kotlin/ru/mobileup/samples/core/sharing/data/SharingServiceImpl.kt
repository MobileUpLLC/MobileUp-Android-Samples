package ru.mobileup.samples.core.sharing.data

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import ru.mobileup.samples.core.error_handling.ExternalAppNotFoundException

class SharingServiceImpl(
    private val context: Context
) : SharingService {

    override fun shareMedia(uri: Uri, mimeType: String) = safeActivityLaunch {
        Intent.createChooser(
            Intent(Intent.ACTION_SEND)
                .setType(mimeType)
                .putExtra(Intent.EXTRA_STREAM, uri)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION).apply {
                    clipData = ClipData.newRawUri("", uri)
                }, null
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    override fun shareText(text: String) = safeActivityLaunch {
        Intent.createChooser(
            Intent(Intent.ACTION_SEND)
                .setType("text/*")
                .putExtra(Intent.EXTRA_TEXT, text), null
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    private fun safeActivityLaunch(intent: () -> Intent) {
        try {
            context.startActivity(intent(), null)
        } catch (e: ActivityNotFoundException) {
            throw ExternalAppNotFoundException(e)
        }
    }
}