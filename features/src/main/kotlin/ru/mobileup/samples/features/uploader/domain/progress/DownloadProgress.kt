package ru.mobileup.samples.features.uploader.domain.progress

sealed interface DownloadProgress {

    data class InProgress(
        val bytesProcessed: Long,
        val bytesTotal: Long
    ) : DownloadProgress

    data object Completed : DownloadProgress
}