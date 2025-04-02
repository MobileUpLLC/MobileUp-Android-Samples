package ru.mobileup.samples.features.document.presentation.preview

import android.net.Uri
import com.arkivanov.decompose.ComponentContext
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import ru.mobileup.samples.core.error_handling.ErrorHandler
import ru.mobileup.samples.core.error_handling.safeRun
import ru.mobileup.samples.core.external_apps.data.ExternalAppService
import ru.mobileup.samples.core.message.data.MessageService
import ru.mobileup.samples.core.message.domain.Message
import ru.mobileup.samples.core.utils.Resource
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.document.data.DocumentManager
import ru.mobileup.samples.features.document.domain.DocumentMetadata

class RealDocumentPreviewComponent(
    private val uri: Uri,
    componentContext: ComponentContext,
    documentManager: DocumentManager,
    private val externalAppService: ExternalAppService,
    private val messageService: MessageService,
    private val errorHandler: ErrorHandler
) : ComponentContext by componentContext, DocumentPreviewComponent {

    override val documentMetadataState = MutableStateFlow<DocumentMetadata?>(null)

    init {
        documentManager.loadMetadata(uri)?.let { documentMetadata ->
            documentMetadataState.update {
                documentMetadata
            }
        } ?: run {
            messageService.showMessage(
                Message(
                    text = StringDesc.Resource(
                        R.string.document_load_failed
                    )
                )
            )
        }
    }

    override fun onOpenClick() {
        safeRun(errorHandler) {
            externalAppService.openFile(
                uri = uri,
                mime = "application/pdf"
            )
        }
    }
}