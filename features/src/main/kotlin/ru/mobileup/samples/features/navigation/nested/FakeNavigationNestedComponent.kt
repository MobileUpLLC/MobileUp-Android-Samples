package ru.mobileup.samples.features.navigation.nested

import com.arkivanov.decompose.router.stack.ChildStack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.utils.createFakeChildStack
import ru.mobileup.samples.features.navigation.nested.NavigationNestedComponent.Child
import ru.mobileup.samples.features.navigation.nested.main.FakeNavigationNestedMainComponent

class FakeNavigationNestedComponent : NavigationNestedComponent {
    override val stack: StateFlow<ChildStack<*, Child>> =
        MutableStateFlow(
            createFakeChildStack(
                Child.Main(
                    FakeNavigationNestedMainComponent()
                )
            )
        )
}
