package ru.mobileup.samples.features.document.data

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import ru.mobileup.samples.features.document.domain.DocumentMetadata
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class DocumentManagerImpl(
    private val context: Context
) : DocumentManager {

    override fun loadMetadata(uri: Uri): DocumentMetadata? {
        return try {
            val projection = arrayOf(
                OpenableColumns.DISPLAY_NAME,
                OpenableColumns.SIZE,
                DocumentsContract.Document.COLUMN_LAST_MODIFIED,
                DocumentsContract.Document.COLUMN_MIME_TYPE,
                DocumentsContract.Document.COLUMN_FLAGS
            )

            val cursor = context.contentResolver.query(
                uri, projection, null, null, null, null
            ) ?: return null

            cursor.use {
                if (it.moveToFirst()) {
                    val name: String = it.getSafeString(OpenableColumns.DISPLAY_NAME)
                    val size = it.getSafeLong(OpenableColumns.SIZE)
                    val mimeType = it.getSafeString(DocumentsContract.Document.COLUMN_MIME_TYPE)
                    val lastModified =
                        it.getSafeLong(DocumentsContract.Document.COLUMN_LAST_MODIFIED)
                    val flags = it.getSafeInt(DocumentsContract.Document.COLUMN_FLAGS)

                    DocumentMetadata(
                        name = name,
                        size = formatFileSize(size),
                        dateModified = formatDateTime(lastModified),
                        mime = mimeType,
                        flags = flags.toString()
                    )
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun Cursor.getSafeString(column: String) = getColumnIndex(column).let { index ->
        if (index != -1) getString(index) else ""
    }

    private fun Cursor.getSafeLong(column: String) = getColumnIndex(column).let { index ->
        if (index != -1) getLong(index) else 0L
    }

    private fun Cursor.getSafeInt(column: String) = getColumnIndex(column).let { index ->
        if (index != -1) getInt(index) else 0
    }

    private fun formatFileSize(sizeBytes: Long): String {
        return when {
            sizeBytes >= 1024 * 1024 -> "%.1fmB".format(sizeBytes / (1024.0 * 1024.0))
            sizeBytes >= 1024 -> "${(sizeBytes / 1024).toInt()}kB"
            else -> "${sizeBytes}B"
        }
    }

    fun formatDateTime(timestampMillis: Long): String {
        val formatter = DateTimeFormatter.ofPattern("MMM dd HH:mm", Locale.getDefault())
        return Instant.ofEpochMilli(timestampMillis)
            .atZone(ZoneId.systemDefault())
            .format(formatter)
    }
}