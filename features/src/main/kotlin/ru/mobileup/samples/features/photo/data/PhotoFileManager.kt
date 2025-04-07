package ru.mobileup.samples.features.photo.data

import android.graphics.Bitmap
import android.net.Uri
import ru.mobileup.samples.features.photo.data.utils.PhotoDirectory

interface PhotoFileManager {
    suspend fun movePhotoToMediaStore(fileUri: Uri): Uri?
    suspend fun cleanPhotoDirectory(directory: PhotoDirectory)
    suspend fun loadBitmapFromUri(uri: Uri): Bitmap?
}