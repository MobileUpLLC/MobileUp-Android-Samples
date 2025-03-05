package ru.mobileup.samples.features.navigation.custom_dialog

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeNavigationCustomDialogComponent : NavigationCustomDialogComponent {
    override val name: StateFlow<String> = MutableStateFlow("")

    override val isSubmitting: StateFlow<Boolean> = MutableStateFlow(true)

    override val isCloseButtonEnabled: StateFlow<Boolean> = MutableStateFlow(true)

    override fun onCloseClick(): Unit = Unit

    override fun onSubmitClick(): Unit = Unit
}
