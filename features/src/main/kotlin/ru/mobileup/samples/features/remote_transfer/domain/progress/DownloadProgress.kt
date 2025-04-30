package ru.mobileup.samples.features.remote_transfer.domain.progress

sealed interface DownloadProgress {

    data class InProgress(
        val bytesProcessed: Long,
        val bytesTotal: Long
    ) : DownloadProgress

    data object Completed : DownloadProgress

    data object Failed : DownloadProgress
}