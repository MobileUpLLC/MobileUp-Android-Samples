package ru.mobileup.samples.features.document.presentation.menu

import android.net.Uri

interface DocumentMenuComponent {

    fun onPreviewClick(uri: Uri?)

    sealed interface Output {
        data class PreviewRequested(val uri: Uri) : Output
    }
}