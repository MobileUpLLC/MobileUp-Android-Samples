package ru.mobileup.samples.features.uploader.data

import kotlinx.coroutines.flow.Flow
import ru.mobileup.samples.features.uploader.domain.progress.DownloadProgress

interface DownloadRepository {
    fun downloadWithKtor(url: String): Flow<DownloadProgress>
    fun downloadWithDownloadManager(url: String)
}