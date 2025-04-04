package ru.mobileup.samples.features.uploader.data

import android.content.Context
import android.net.Uri
import io.ktor.client.HttpClient
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.forms.InputProvider
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.streams.asInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import ru.mobileup.samples.features.uploader.domain.progress.UploadProgress
import java.io.FileNotFoundException

private const val BASE_URL = "https://0x0.st/"
private const val FILE_KEY = "file"

class UploadRepositoryImpl(
    private val context: Context
) : UploadRepository {

    override fun upload(uri: Uri): Flow<UploadProgress> = channelFlow {
        val contentResolver = context.contentResolver
        val descriptor =
            contentResolver.openAssetFileDescriptor(uri, "r") ?: throw FileNotFoundException()
        val fileSize = descriptor.length
        val mimeType = contentResolver.getType(uri) ?: ""

        descriptor.close()

        val result = HttpClient().submitFormWithBinaryData(
            url = BASE_URL,
            formData = formData {
                append(
                    FILE_KEY, InputProvider(
                        fileSize
                    ) { context.contentResolver.openInputStream(uri)!!.asInput() }, Headers.build {
                        append(HttpHeaders.ContentType, mimeType)
                        append(HttpHeaders.ContentDisposition, "filename=test")
                    })
            }
        ) {
            onUpload { bytesSent, bytesTotal ->
                if (bytesTotal != null) {
                    send(
                        UploadProgress.Uploading(
                            bytesProcessed = bytesSent,
                            bytesTotal = bytesTotal
                        )
                    )
                }
            }
        }

        send(UploadProgress.Completed(result.bodyAsText()))
    }
}