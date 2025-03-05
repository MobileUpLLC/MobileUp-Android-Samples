package ru.mobileup.samples.features.navigation

import com.arkivanov.decompose.ComponentContext
import dev.icerock.moko.resources.desc.StringDesc
import org.koin.core.component.get
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.features.navigation.alert_dialogs.NavigationAlertDialogsComponent
import ru.mobileup.samples.features.navigation.alert_dialogs.RealNavigationAlertDialogsComponent
import ru.mobileup.samples.features.navigation.bottom_sheets.NavigationBottomSheetsComponent
import ru.mobileup.samples.features.navigation.bottom_sheets.RealNavigationBottomSheetsComponent
import ru.mobileup.samples.features.navigation.custom_dialog.NavigationCustomDialogComponent
import ru.mobileup.samples.features.navigation.custom_dialog.RealNavigationCustomDialogComponent
import ru.mobileup.samples.features.navigation.nested.NavigationNestedComponent
import ru.mobileup.samples.features.navigation.nested.RealNavigationNestedComponent
import ru.mobileup.samples.features.navigation.nested.leaf.NavigationNestedLeafComponent
import ru.mobileup.samples.features.navigation.nested.leaf.RealNavigationNestedLeafComponent
import ru.mobileup.samples.features.navigation.nested.main.NavigationNestedMainComponent
import ru.mobileup.samples.features.navigation.nested.main.RealNavigationNestedMainComponent

fun ComponentFactory.createNavigationComponent(
    componentContext: ComponentContext
): NavigationComponent {
    return RealNavigationComponent(componentContext, get())
}

fun ComponentFactory.createNavigationBottomSheetsComponent(
    componentContext: ComponentContext
): NavigationBottomSheetsComponent {
    return RealNavigationBottomSheetsComponent(componentContext, get())
}

fun ComponentFactory.createNavigationNestedComponent(
    componentContext: ComponentContext
): NavigationNestedComponent {
    return RealNavigationNestedComponent(componentContext, get())
}

fun ComponentFactory.createNavigationAlertDialogsComponent(
    componentContext: ComponentContext,
): NavigationAlertDialogsComponent {
    return RealNavigationAlertDialogsComponent(componentContext, get())
}

fun ComponentFactory.createNavigationCustomDialogComponent(
    componentContext: ComponentContext,
    config: NavigationCustomDialogComponent.Config,
    onOutput: (NavigationCustomDialogComponent.Output) -> Unit,
): NavigationCustomDialogComponent {
    return RealNavigationCustomDialogComponent(
        componentContext,
        config,
        onOutput,
        get()
    )
}

fun ComponentFactory.createNavigationNestedLeafComponent(
    name: StringDesc
): NavigationNestedLeafComponent {
    return RealNavigationNestedLeafComponent(name)
}

fun ComponentFactory.createNavigationNestedMainComponent(
    onOutput: (NavigationNestedMainComponent.Output) -> Unit
): NavigationNestedMainComponent {
    return RealNavigationNestedMainComponent(onOutput)
}
