package ru.mobileup.samples.features.document.presentation

import android.net.Uri
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import kotlinx.serialization.Serializable
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.core.utils.UriSerializer
import ru.mobileup.samples.core.utils.safePush
import ru.mobileup.samples.core.utils.toStateFlow
import ru.mobileup.samples.features.document.createDocumentMenuComponent
import ru.mobileup.samples.features.document.createDocumentPreviewComponent
import ru.mobileup.samples.features.document.presentation.menu.DocumentMenuComponent

class RealDocumentComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory
) : ComponentContext by componentContext, DocumentComponent {

    private val navigation = StackNavigation<ChildConfig>()

    override val childStack = childStack(
        source = navigation,
        initialConfiguration = ChildConfig.Menu,
        serializer = ChildConfig.serializer(),
        handleBackButton = true,
        childFactory = ::createChild
    ).toStateFlow(lifecycle)

    private fun createChild(
        config: ChildConfig,
        componentContext: ComponentContext
    ): DocumentComponent.Child = when (config) {
        is ChildConfig.Menu -> {
            DocumentComponent.Child.Menu(
                componentFactory.createDocumentMenuComponent(
                    componentContext,
                    ::onMenuOutput
                )
            )
        }

        is ChildConfig.Preview -> {
            DocumentComponent.Child.Preview(
                componentFactory.createDocumentPreviewComponent(
                    config.uri,
                    componentContext
                )
            )
        }
    }

    private fun onMenuOutput(output: DocumentMenuComponent.Output) {
        when (output) {
            is DocumentMenuComponent.Output.PreviewRequested -> navigation.safePush(
                ChildConfig.Preview(
                    output.uri
                )
            )
        }
    }

    @Serializable
    sealed interface ChildConfig {

        @Serializable
        data object Menu : ChildConfig

        @Serializable
        data class Preview(
            @Serializable(with = UriSerializer::class) val uri: Uri
        ) : ChildConfig
    }
}