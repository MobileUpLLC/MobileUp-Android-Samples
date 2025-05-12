package ru.mobileup.samples.features.remote_transfer.domain.progress

import android.net.Uri

sealed interface UploadProgress {

    val uri: Uri

    data class Uploading(
        override val uri: Uri,
        val bytesProcessed: Long,
        val bytesTotal: Long
    ) : UploadProgress

    data class Completed(
        override val uri: Uri,
        val link: String,
    ) : UploadProgress

    data class Failed(
        override val uri: Uri
    ) : UploadProgress
}