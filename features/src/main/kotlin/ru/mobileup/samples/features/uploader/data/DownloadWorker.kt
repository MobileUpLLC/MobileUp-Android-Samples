package ru.mobileup.samples.features.uploader.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ru.mobileup.samples.features.R
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.URL

private const val APP_DIRECTORY = "MobileUp"
private const val RELATIVE_STORAGE_PATH = "Download/$APP_DIRECTORY"
private const val PROGRESS_MAX_VALUE = 100
private const val PROGRESS_UPDATE_DELAY_MS = 100L

class DownloadWorker(
    private val context: Context,
    private val workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        private const val CHANNEL_ID = "download_worker_id"
        private const val NOTIFICATION_ID = 123
        const val KEY_FILE_URL = "key_file_url"
        const val KEY_FILE_URI = "key_file_uri"
        const val KEY_DOWNLOAD_PROGRESS = "key_download_progress"
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun doWork(): Result {
        val url = workerParams.inputData.getString(KEY_FILE_URL)

        url?.let {
            val fileName = url.split("/").last().replace("\n", "")
            val fileType = fileName.split(".").last().uppercase()
            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileType)
                ?: return Result.failure()

            return try {
                val uri = downloadFileFromUri(url, mimeType, fileName)
                notificationManager.cancel(NOTIFICATION_ID)

                if (uri != null) {
                    Result.success(workDataOf(KEY_FILE_URI to uri.toString()))
                } else {
                    Result.failure()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure()
            }
        }
        return Result.failure()
    }

    private fun downloadFileFromUri(url: String, mimeType: String, filename: String?): Uri? {
        val connection = URL(url).openConnection()
        val contentLength = connection.contentLength.toLong()

        registerNotificationChannel()
        displayNotification(0, contentLength)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                put(MediaStore.MediaColumns.RELATIVE_PATH, RELATIVE_STORAGE_PATH)
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

            if (uri != null) {
                connection.getInputStream().use { input ->
                    resolver.openOutputStream(uri).use { output ->
                        processDownload(input, output, contentLength)
                    }
                }
                uri
            } else {
                null
            }
        } else {
            val target = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "$APP_DIRECTORY/$filename"
            )

            connection.getInputStream().use { input ->
                FileOutputStream(target).use { output ->
                    processDownload(input, output, contentLength)
                }
            }

            target.toUri()
        }
    }

    private fun processDownload(
        input: InputStream,
        output: OutputStream?,
        contentLength: Long
    ) {
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var bytes = input.read(buffer)
        var bytesDownloaded = 0L
        var lastUpdateTime = 0L

        while (bytes >= 0) {
            output?.write(buffer, 0, bytes)
            bytesDownloaded += bytes
            bytes = input.read(buffer)

            val currentTime = System.currentTimeMillis()
            if (currentTime - lastUpdateTime < PROGRESS_UPDATE_DELAY_MS) {
                continue
            }
            lastUpdateTime = currentTime

            scope.launch {
                displayNotification(bytesDownloaded, contentLength)

                setProgress(
                    workDataOf(
                        KEY_DOWNLOAD_PROGRESS to arrayOf(
                            bytesDownloaded,
                            contentLength
                        )
                    )
                )
            }
        }
    }

    private fun registerNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_ID,
            NotificationManager.IMPORTANCE_LOW
        )
        channel.enableVibration(false)
        notificationManager.createNotificationChannel(channel)
    }

    private fun displayNotification(
        bytesDownloaded: Long,
        contentLength: Long,
    ) {
        val progress = ((bytesDownloaded * PROGRESS_MAX_VALUE) / contentLength).toInt()

        val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_app_logo)
            .setContentTitle(context.getString(R.string.uploader_download_title))
            .setContentText(context.getString(R.string.uploader_download_description))
            .setProgress(PROGRESS_MAX_VALUE, progress, false)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }
}