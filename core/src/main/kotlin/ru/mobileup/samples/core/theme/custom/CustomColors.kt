package ru.mobileup.samples.core.theme.custom

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class CustomColors(
    val isLight: Boolean,
    val background: BackgroundColors,
    val text: TextColors,
    val icon: IconColors,
    val palette: PaletteColors,
    val button: ButtonColors,
    val border: BorderColors,
    val textField: TextFieldColors,
)

data class BackgroundColors(
    val screen: Color,
    val secondary: Color,
    val toast: Color,
)

data class TextColors(
    val primary: Color,
    val primaryDisabled: Color,
    val secondary: Color,
    val secondaryDisabled: Color,
    val invert: Color,
    val invertDisabled: Color,
    val warning: Color,
    val error: Color
)

data class IconColors(
    val primary: Color,
    val primaryDisabled: Color,
    val secondary: Color,
    val invert: Color,
    val warning: Color,
    val error: Color,
)

data class PaletteColors(
    val white: Color,
    val white50: Color,
    val white10: Color,
    val black: Color,
    val black50: Color,
    val black10: Color,
    val grayscale: ExtendedPaletteColor
)

data class ExtendedPaletteColor(
    val l900: Color
)

data class ButtonColors(
    val primary: Color,
    val primaryDisabled: Color,
    val secondary: Color,
    val secondaryDisabled: Color,
)

data class TextFieldColors(
    val background: Color,
    val backgroundDisabled: Color,
)

data class BorderColors(
    val primary: Color,
    val error: Color,
)

val LocalCustomColors = staticCompositionLocalOf<CustomColors?> { null }
