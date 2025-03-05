package ru.mobileup.samples.features.navigation

import com.arkivanov.decompose.router.stack.ChildStack
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.strResDesc
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.navigation.alert_dialogs.NavigationAlertDialogsComponent
import ru.mobileup.samples.features.navigation.bottom_sheets.NavigationBottomSheetsComponent
import ru.mobileup.samples.features.navigation.nested.NavigationNestedComponent

interface NavigationComponent {

    val stack: StateFlow<ChildStack<*, Child>>

    val selectedTab: StateFlow<Tab>
    val isBottomBarVisible: StateFlow<Boolean>

    fun onTabSelect(tab: Tab)
    fun onBottomBarVisibilityChange(isVisible: Boolean)

    enum class Tab {
        Nested,
        AlertDialogs,
        BottomSheets;

        val nameForDisplay: StringDesc
            get() = when (this) {
                Nested -> R.string.navigation_nested_navigation
                BottomSheets -> R.string.navigation_bottom_sheets
                AlertDialogs -> R.string.navigation_alert_dialogs
            }.strResDesc()
    }

    sealed interface Child {
        class Nested(val component: NavigationNestedComponent) : Child
        class BottomSheets(val component: NavigationBottomSheetsComponent) : Child
        class AlertDialogs(val component: NavigationAlertDialogsComponent) : Child
    }
}