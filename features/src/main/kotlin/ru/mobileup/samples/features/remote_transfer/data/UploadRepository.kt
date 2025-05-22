package ru.mobileup.samples.features.remote_transfer.data

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import ru.mobileup.samples.features.remote_transfer.domain.progress.UploadProgress

interface UploadRepository {
    fun upload(uri: Uri): Flow<UploadProgress>
}