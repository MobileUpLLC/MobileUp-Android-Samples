package ru.mobileup.samples.features.uploader.domain.progress

sealed interface UploadProgress {

    data class Uploading(
        val bytesProcessed: Long,
        val bytesTotal: Long
    ) : UploadProgress

    data class Completed(
        val link: String
    ) : UploadProgress
}