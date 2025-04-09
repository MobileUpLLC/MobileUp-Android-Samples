package ru.mobileup.samples.features.photo.data

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.exifinterface.media.ExifInterface
import co.touchlab.kermit.Logger
import coil3.BitmapImage
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.mobileup.samples.features.photo.data.utils.PhotoDirectory
import ru.mobileup.samples.features.photo.data.utils.getPhotoFileName
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

private const val TAG = "PhotoFileManager"
private const val PHOTO_MIME_TYPE = "image/jpeg"
private const val APP_DIRECTORY = "MobileUp"
private const val RELATIVE_STORAGE_PATH = "Pictures/$APP_DIRECTORY"
private const val QUALITY_ORIGINAL = 100

class PhotoFileManagerImpl(
    private val context: Context
) : PhotoFileManager {

    override suspend fun movePhotoToMediaStore(fileUri: Uri): Uri? {
        return withContext(Dispatchers.IO) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                moveFileToMediaStoreApi29(fileUri)
            } else {
                moveFileToMediaStore(fileUri)
            }
        }
    }

    override suspend fun cleanPhotoDirectory(directory: PhotoDirectory) {
        withContext(Dispatchers.IO) {
            try {
                directory
                    .toFile(context)
                    .deleteRecursively()
            } catch (_: Exception) {
                // Do nothing
            }
        }
    }

    override suspend fun loadBitmapFromUri(uri: Uri): Bitmap? {
        val request = ImageRequest.Builder(context)
            .data(uri)
            .build()
        val result = context.imageLoader.execute(request)
        val drawable = (result as? SuccessResult)?.image
        return (drawable as? BitmapImage)?.bitmap
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private fun moveFileToMediaStoreApi29(uri: Uri): Uri? {
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, getPhotoFileName())
            put(MediaStore.MediaColumns.MIME_TYPE, PHOTO_MIME_TYPE)
            put(MediaStore.Images.Media.RELATIVE_PATH, RELATIVE_STORAGE_PATH)
        }

        val newUri = resolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )

        return try {
            resolver.openInputStream(uri)?.use { inputStream ->
                newUri?.let { resolver.openOutputStream(it) }?.use { outputStream ->
                    writeFile(
                        inputStream = inputStream,
                        outputStream = outputStream,
                        exifInterface = uri.path?.let {
                            ExifInterface(it)
                        }
                    )
                    uri
                }
            }
        } catch (e: Exception) {
            Logger.withTag(TAG).e("Photo failed $e")
            null
        }
    }

    private fun moveFileToMediaStore(uri: Uri): Uri? {
        val appDirectory = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            APP_DIRECTORY
        )

        if (!appDirectory.exists()) {
            appDirectory.mkdirs()
        }

        val newFile = File(appDirectory, getPhotoFileName())

        return try {
            FileInputStream(uri.toFile()).use { inputStream ->
                FileOutputStream(newFile).use { outputStream ->
                    writeFile(
                        inputStream = inputStream,
                        outputStream = outputStream,
                        exifInterface = uri.path?.let {
                            ExifInterface(it)
                        }
                    )
                }
            }
            newFile.toUri()
        } catch (e: Exception) {
            Logger.withTag(TAG).e("Photo failed $e")
            null
        }
    }

    private fun writeFile(
        inputStream: InputStream,
        outputStream: OutputStream,
        exifInterface: ExifInterface?
    ) {
        val orientation = exifInterface?.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )

        when (orientation) {
            // Некоторые девайсы могут не поддерживать эти два аттрибута
            // Для корректного отображения в других приложениях лучше повернуть Bitmap самостоятельно
            ExifInterface.ORIENTATION_TRANSVERSE -> BitmapFactory
                .decodeStream(inputStream)
                .flipHorizontal()
                .compress(Bitmap.CompressFormat.JPEG, QUALITY_ORIGINAL, outputStream)

            ExifInterface.ORIENTATION_TRANSPOSE -> BitmapFactory
                .decodeStream(inputStream)
                .flipVertical()
                .compress(Bitmap.CompressFormat.JPEG, QUALITY_ORIGINAL, outputStream)

            else -> inputStream.copyTo(outputStream)
        }
    }

    private fun Bitmap.flipHorizontal() =
        Bitmap.createBitmap(this, 0, 0, this.width, this.height, Matrix().apply {
            postRotate(270f)
            postScale(-1f, 1f)
        }, true)

    private fun Bitmap.flipVertical() =
        Bitmap.createBitmap(this, 0, 0, this.width, this.height, Matrix().apply {
            postRotate(90f)
            postScale(-1f, 1f)
        }, true)
}