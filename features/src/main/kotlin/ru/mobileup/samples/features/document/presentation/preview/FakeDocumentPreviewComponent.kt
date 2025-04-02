package ru.mobileup.samples.features.document.presentation.preview

import kotlinx.coroutines.flow.MutableStateFlow

class FakeDocumentPreviewComponent : DocumentPreviewComponent {
    override val documentMetadataState = MutableStateFlow(null)
    override fun onOpenClick() = Unit
}