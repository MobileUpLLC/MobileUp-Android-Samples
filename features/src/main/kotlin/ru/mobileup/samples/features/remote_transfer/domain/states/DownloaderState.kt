package ru.mobileup.samples.features.remote_transfer.domain.states

import ru.mobileup.samples.features.remote_transfer.domain.progress.DownloadProgress

data class DownloaderState(
    val selectedLink: String = "",
    val isValid: Boolean = false,
    val downloadProgress: DownloadProgress? = null
)