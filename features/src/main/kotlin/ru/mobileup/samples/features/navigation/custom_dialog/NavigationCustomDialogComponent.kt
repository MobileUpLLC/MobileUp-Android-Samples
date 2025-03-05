package ru.mobileup.samples.features.navigation.custom_dialog

import kotlinx.coroutines.flow.StateFlow

interface NavigationCustomDialogComponent {
    val name: StateFlow<String>
    val isSubmitting: StateFlow<Boolean>
    val isCloseButtonEnabled: StateFlow<Boolean>

    fun onCloseClick()
    fun onSubmitClick()

    data class Config(val userName: String)

    sealed interface Output {
        data object CloseRequest : Output
        data object SubmitRequest : Output
    }
}