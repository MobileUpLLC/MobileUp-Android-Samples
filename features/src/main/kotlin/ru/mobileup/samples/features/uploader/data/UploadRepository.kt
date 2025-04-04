package ru.mobileup.samples.features.uploader.data

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import ru.mobileup.samples.features.uploader.domain.progress.UploadProgress

interface UploadRepository {
    fun upload(uri: Uri): Flow<UploadProgress>
}