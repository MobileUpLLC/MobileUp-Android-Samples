package ru.mobileup.samples.features.remote_transfer.domain.progress

sealed interface UploadProgress {

    data class Uploading(
        val bytesProcessed: Long,
        val bytesTotal: Long
    ) : UploadProgress

    data class Completed(
        val link: String
    ) : UploadProgress

    data object Failed : UploadProgress
}