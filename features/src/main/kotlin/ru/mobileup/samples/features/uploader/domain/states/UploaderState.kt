package ru.mobileup.samples.features.uploader.domain.states

import android.net.Uri
import ru.mobileup.samples.features.uploader.domain.progress.DownloadProgress
import ru.mobileup.samples.features.uploader.domain.progress.UploadProgress

data class UploaderState(
    val uri: Uri?,
    val uploadProgress: UploadProgress?,
    val downloadProgress: DownloadProgress?
) {
    companion object {
        fun build() = UploaderState(
            uri = null,
            uploadProgress = null,
            downloadProgress = null
        )
    }
}