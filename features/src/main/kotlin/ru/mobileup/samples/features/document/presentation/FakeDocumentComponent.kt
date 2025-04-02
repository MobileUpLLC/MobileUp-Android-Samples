package ru.mobileup.samples.features.document.presentation

import ru.mobileup.samples.core.utils.createFakeChildStackStateFlow
import ru.mobileup.samples.features.document.presentation.menu.FakeDocumentMenuComponent

class FakeDocumentComponent : DocumentComponent {

    override val childStack = createFakeChildStackStateFlow(
        DocumentComponent.Child.Menu(FakeDocumentMenuComponent())
    )
}