package ru.mobileup.samples.features.uploader.data

import android.app.DownloadManager
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.prepareGet
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import ru.mobileup.samples.features.uploader.domain.progress.DownloadProgress
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

private const val APP_DIRECTORY = "MobileUp"
private const val RELATIVE_STORAGE_PATH = "Download/$APP_DIRECTORY"
private const val BUFFER_SIZE = 8 * 1024

class DownloadRepositoryImpl(
    private val context: Context
) : DownloadRepository {

    override fun downloadWithKtor(url: String): Flow<DownloadProgress> = channelFlow {
        withContext(Dispatchers.IO) {
            val fileName = url.split("/").last()
            val file = File(context.cacheDir, fileName.replace("\n", ""))

            HttpClient().prepareGet(url) {
                onDownload { bytesProcessed, bytesTotal ->
                    if (bytesTotal != null) {
                        send(
                            DownloadProgress.InProgress(
                                bytesProcessed = bytesProcessed,
                                bytesTotal = bytesTotal
                            )
                        )
                    }
                }
            }.execute { response ->
                val channel: ByteReadChannel = response.body()

                file.outputStream().use { output ->
                    val buffer = ByteArray(BUFFER_SIZE)

                    while (!channel.isClosedForRead) {
                        val readBytes = channel.readAvailable(buffer)
                        if (readBytes > 0) {
                            output.write(buffer, 0, readBytes)
                            output.flush()
                        }
                    }
                }
            }

            moveFileToMediaStore(file)
            send(DownloadProgress.Completed)
        }
    }

    override fun downloadWithDownloadManager(url: String) {
        val fileName = url.split("/").last().replace("\n", "")

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(url)).apply {
            setTitle("Download file")
            setDescription("Download in progress")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "$APP_DIRECTORY/$fileName"
            )
        }

        downloadManager.enqueue(request)
    }

    private suspend fun moveFileToMediaStore(file: File): Uri? {
        return withContext(Dispatchers.IO) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                moveFileToMediaStoreApi29(file)
            } else {
                moveFileToMediaStoreDeprecated(file)
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private fun moveFileToMediaStoreApi29(file: File): Uri? {
        val resolver = context.contentResolver

        val uri: Uri = Uri.fromFile(file)
        val mimeType = if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
            resolver.getType(uri)
        } else {
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension.lowercase())
        }

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.Images.Media.RELATIVE_PATH, RELATIVE_STORAGE_PATH)
        }

        val newUri = resolver.insert(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
            contentValues
        )

        return try {
            resolver.openInputStream(uri)?.use { inputStream ->
                newUri?.let { resolver.openOutputStream(it) }?.use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            newUri
        } catch (e: Exception) {
            null
        }
    }

    private fun moveFileToMediaStoreDeprecated(file: File): Uri? {
        val appDirectory = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            APP_DIRECTORY
        )

        if (!appDirectory.exists()) {
            appDirectory.mkdirs()
        }

        val newFile = File(appDirectory, file.name)

        return try {
            FileInputStream(file).use { inputStream ->
                FileOutputStream(newFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            newFile.toUri()
        } catch (e: Exception) {
            null
        }
    }
}