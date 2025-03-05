package ru.mobileup.samples.features.navigation.nested.leaf

import dev.icerock.moko.resources.desc.Raw
import dev.icerock.moko.resources.desc.StringDesc

class FakeNavigationNestedLeafComponent : NavigationNestedLeafComponent {
    override val name: StringDesc = StringDesc.Raw("Fake")
}
