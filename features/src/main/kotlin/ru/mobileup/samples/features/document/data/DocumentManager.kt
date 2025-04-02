package ru.mobileup.samples.features.document.data

import android.net.Uri
import ru.mobileup.samples.features.document.domain.DocumentMetadata

interface DocumentManager {

    fun loadMetadata(uri: Uri): DocumentMetadata?
}