package ru.mobileup.samples.features.document

import android.net.Uri
import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import org.koin.dsl.module
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.features.document.data.DocumentManager
import ru.mobileup.samples.features.document.data.DocumentManagerImpl
import ru.mobileup.samples.features.document.presentation.DocumentComponent
import ru.mobileup.samples.features.document.presentation.RealDocumentComponent
import ru.mobileup.samples.features.document.presentation.menu.DocumentMenuComponent
import ru.mobileup.samples.features.document.presentation.menu.RealDocumentMenuComponent
import ru.mobileup.samples.features.document.presentation.preview.DocumentPreviewComponent
import ru.mobileup.samples.features.document.presentation.preview.RealDocumentPreviewComponent

val documentModule = module {
    single<DocumentManager> { DocumentManagerImpl(get()) }
}

fun ComponentFactory.createDocumentComponent(componentContext: ComponentContext): DocumentComponent {
    return RealDocumentComponent(componentContext, get())
}

fun ComponentFactory.createDocumentMenuComponent(
    componentContext: ComponentContext,
    onOutput: (DocumentMenuComponent.Output) -> Unit
): DocumentMenuComponent {
    return RealDocumentMenuComponent(componentContext, onOutput, get())
}

fun ComponentFactory.createDocumentPreviewComponent(
    uri: Uri,
    componentContext: ComponentContext
): DocumentPreviewComponent {
    return RealDocumentPreviewComponent(uri, componentContext, get(), get(), get(), get(), get())
}