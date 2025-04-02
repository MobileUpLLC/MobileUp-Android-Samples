package ru.mobileup.samples.core.external_apps.data

import android.net.Uri
import ru.mobileup.samples.core.error_handling.ExternalAppNotFoundException

interface ExternalAppService {

    @Throws(ExternalAppNotFoundException::class)
    fun openFile(uri: Uri, mime: String)

    @Throws(ExternalAppNotFoundException::class)
    fun openUrl(url: String)

    @Throws(ExternalAppNotFoundException::class)
    suspend fun openPdfFromAssets(fileName: String)

    suspend fun openPhone(phone: String)

    fun openAppSettings()

    fun openLocationSettings()

    fun openNotificationSettings()
}
