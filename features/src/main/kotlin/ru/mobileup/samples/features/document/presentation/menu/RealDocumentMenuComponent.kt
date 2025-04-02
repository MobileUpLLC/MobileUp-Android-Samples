package ru.mobileup.samples.features.document.presentation.menu

import android.net.Uri
import com.arkivanov.decompose.ComponentContext
import dev.icerock.moko.resources.desc.StringDesc
import ru.mobileup.samples.core.message.data.MessageService
import ru.mobileup.samples.core.message.domain.Message
import ru.mobileup.samples.core.utils.Resource
import ru.mobileup.samples.features.R

class RealDocumentMenuComponent(
    componentContext: ComponentContext,
    private val onOutput: (DocumentMenuComponent.Output) -> Unit,
    private val messageService: MessageService
) : ComponentContext by componentContext, DocumentMenuComponent {

    override fun onPreviewClick(uri: Uri?) {
        if (uri == null) {
            messageService.showMessage(
                Message(
                    text = StringDesc.Resource(
                        R.string.document_not_found
                    )
                )
            )
        } else {
            onOutput(DocumentMenuComponent.Output.PreviewRequested(uri))
        }
    }
}