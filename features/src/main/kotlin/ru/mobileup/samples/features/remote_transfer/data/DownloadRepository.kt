package ru.mobileup.samples.features.remote_transfer.data

import kotlinx.coroutines.flow.Flow
import ru.mobileup.samples.features.remote_transfer.domain.progress.DownloadProgress

interface DownloadRepository {
    fun downloadWithKtor(url: String): Flow<DownloadProgress>
    fun downloadWithDownloadManager(url: String)
    fun downloadWithWorkManager(url: String): Flow<DownloadProgress>
}