package ru.mobileup.samples.features.root.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import ru.mobileup.samples.core.message.presentation.MessageUi
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.utils.LocalSystemBarsSettings
import ru.mobileup.samples.core.utils.accumulate
import ru.mobileup.samples.features.calendar.presentation.CalendarUi
import ru.mobileup.samples.features.charts.presentation.ChartUi
import ru.mobileup.samples.features.form.presentation.FormUi
import ru.mobileup.samples.features.menu.presentation.MenuUi
import ru.mobileup.samples.features.qr_code.presentation.generator.QrCodeGeneratorUi
import ru.mobileup.samples.features.qr_code.presentation.scanner.QrCodeScannerUi
import ru.mobileup.samples.features.navigation.NavigationUi
import ru.mobileup.samples.features.video.presentation.VideoUi

@Suppress("ModifierReused")
@Composable
fun RootUi(
    component: RootComponent,
    modifier: Modifier = Modifier
) {
    val childStack by component.childStack.collectAsState()

    SystemBarsColors()

    Children(childStack, modifier) { child ->
        when (val instance = child.instance) {
            is RootComponent.Child.Menu -> MenuUi(instance.component)
            is RootComponent.Child.Form -> FormUi(instance.component)
            is RootComponent.Child.Video -> VideoUi(instance.component)
            is RootComponent.Child.Calendar -> CalendarUi(instance.component)
            is RootComponent.Child.QrCodeGenerator -> QrCodeGeneratorUi(instance.component)
            is RootComponent.Child.QrCodeScanner -> QrCodeScannerUi(instance.component)
            is RootComponent.Child.Chart -> ChartUi(instance.component)
            is RootComponent.Child.Navigation -> NavigationUi(instance.component)
        }
    }

    MessageUi(
        component = component.messageComponent,
        modifier = modifier,
        bottomPadding = 16.dp
    )
}

@Composable
private fun SystemBarsColors() {
    val systemUiController = rememberSystemUiController()
    val systemBarsSettings = LocalSystemBarsSettings.current.accumulate()

    val statusBarColor = Color.Transparent

    val navigationBarColor = when (systemBarsSettings.transparentNavigationBar) {
        true -> Color.Transparent
        false -> CustomTheme.colors.background.screen
    }

    val darkStatusBarIcons = CustomTheme.colors.isLight && !systemBarsSettings.lightStatusBarIcons

    val darkNavigationBarIcons = CustomTheme.colors.isLight

    LaunchedEffect(statusBarColor, darkStatusBarIcons) {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = darkStatusBarIcons
        )
    }

    LaunchedEffect(navigationBarColor, darkNavigationBarIcons) {
        systemUiController.setNavigationBarColor(
            color = navigationBarColor,
            darkIcons = darkNavigationBarIcons,
            navigationBarContrastEnforced = false
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun RootUiPreview() {
    AppTheme {
        RootUi(FakeRootComponent())
    }
}