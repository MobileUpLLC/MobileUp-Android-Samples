package ru.mobileup.samples.features.navigation.custom_dialog

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import ru.mobileup.samples.core.error_handling.ErrorHandler
import ru.mobileup.samples.core.error_handling.safeLaunch
import ru.mobileup.samples.core.utils.componentScope
import ru.mobileup.samples.core.utils.computed
import ru.mobileup.samples.core.utils.withProgress

class RealNavigationCustomDialogComponent(
    componentContext: ComponentContext,
    config: NavigationCustomDialogComponent.Config,
    private val onOutput: (NavigationCustomDialogComponent.Output) -> Unit,
    private val errorHandler: ErrorHandler
) : ComponentContext by componentContext, NavigationCustomDialogComponent {

    override val name = MutableStateFlow(config.userName)
    override val isSubmitting = MutableStateFlow(false)
    override val isCloseButtonEnabled = computed(isSubmitting, Boolean::not)

    override fun onCloseClick() {
        if (isSubmitting.value) return
        onOutput(NavigationCustomDialogComponent.Output.CloseRequest)
    }

    override fun onSubmitClick() {
        componentScope.safeLaunch(errorHandler) {
            withProgress(isSubmitting) {
                delay(2000)
                onOutput(NavigationCustomDialogComponent.Output.SubmitRequest)
            }
        }
    }
}