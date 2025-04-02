package ru.mobileup.samples.features.document.presentation.preview

import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.features.document.domain.DocumentMetadata

interface DocumentPreviewComponent {
    val documentMetadataState: StateFlow<DocumentMetadata?>

    fun onOpenClick()
}