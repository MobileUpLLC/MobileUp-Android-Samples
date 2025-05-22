package ru.mobileup.samples.features.divkit.presentation

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.essenty.backhandler.BackHandler
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.utils.createFakeChildStackStateFlow
import ru.mobileup.samples.core.utils.fakeBackHandler
import ru.mobileup.samples.features.divkit.presentation.examples_list.FakeDivKitExamplesListComponent

class FakeDivKitComponent : DivKitComponent {
    override val childStack: StateFlow<ChildStack<*, DivKitComponent.Child>> =
        createFakeChildStackStateFlow(DivKitComponent.Child.ExamplesList(FakeDivKitExamplesListComponent()))

    override fun onBackClick() = Unit

    override val backHandler: BackHandler = fakeBackHandler
}