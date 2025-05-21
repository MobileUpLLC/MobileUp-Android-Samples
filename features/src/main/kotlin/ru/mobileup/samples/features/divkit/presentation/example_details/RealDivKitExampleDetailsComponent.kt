package ru.mobileup.samples.features.divkit.presentation.example_details

import android.net.Uri
import com.arkivanov.decompose.ComponentContext
import com.yandex.div.core.Div2Context
import com.yandex.div.core.DivActionHandler
import com.yandex.div.core.DivViewFacade
import com.yandex.div.core.view2.Div2View
import com.yandex.div.json.expressions.ExpressionResolver
import com.yandex.div2.DivAction
import dev.icerock.moko.resources.desc.Raw
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import ru.mobileup.samples.core.error_handling.ErrorHandler
import ru.mobileup.samples.core.error_handling.safeLaunch
import ru.mobileup.samples.core.message.data.MessageService
import ru.mobileup.samples.core.message.domain.Message
import ru.mobileup.samples.core.utils.componentScope
import ru.mobileup.samples.features.divkit.data.DivKitRepository
import ru.mobileup.samples.features.divkit.presentation.extension.tag
import androidx.core.net.toUri

class RealDivKitExampleDetailsComponent(
    componentContext: ComponentContext,
    divkitContext: Div2Context,
    override val title: String,
    override val jsonName: String,
    divKitRepository: DivKitRepository,
    errorHandler: ErrorHandler,
    private val messageService: MessageService
) : ComponentContext by componentContext, DivKitExampleDetailsComponent {

    override val content = MutableStateFlow<Div2View?>(null)

    init {
        componentScope.safeLaunch(errorHandler) {
            val divKitData = divKitRepository.getExampleData(jsonName)
            content.update {
                Div2View(divkitContext).apply {
                    setData(divKitData, divKitData.tag)
                    actionHandler = createActionHandler()
                }
            }
        }
    }

    private fun createActionHandler() = object : DivActionHandler() {
        override fun handleAction(
            action: DivAction,
            view: DivViewFacade,
            resolver: ExpressionResolver
        ): Boolean {
            val url = action.url?.evaluate(resolver).toString()
            when {
                url.contains("div-action://sign_up") -> onSignUpClick(url.toUri())
                else -> Unit
            }
            return super.handleAction(action, view, resolver)
        }
    }

    private fun onSignUpClick(uri: Uri) {
        val credentials = Credentials.fromUri(uri)
        messageService.showMessage(
            Message(
                text = StringDesc.Raw(
                    "Data parsed! $credentials"
                ),
            )
        )
    }
}

private data class Credentials(
    val email: String,
    val password: String
) {
    companion object {
        fun fromUri(uri: Uri) = Credentials(
            email = uri.getQueryParameter("email") ?: "",
            password = uri.getQueryParameter("password") ?: ""
        )
    }
}