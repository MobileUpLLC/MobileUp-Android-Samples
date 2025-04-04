package ru.mobileup.samples.core.theme.values

import androidx.compose.ui.graphics.Color
import ru.mobileup.samples.core.theme.custom.BackgroundColors
import ru.mobileup.samples.core.theme.custom.BorderColors
import ru.mobileup.samples.core.theme.custom.ButtonColors
import ru.mobileup.samples.core.theme.custom.CommonColors
import ru.mobileup.samples.core.theme.custom.CustomColors
import ru.mobileup.samples.core.theme.custom.ExtendedPaletteColor
import ru.mobileup.samples.core.theme.custom.IconColors
import ru.mobileup.samples.core.theme.custom.PaletteColors
import ru.mobileup.samples.core.theme.custom.TextColors
import ru.mobileup.samples.core.theme.custom.TextFieldColors

val LightAppColors = CustomColors(
    isLight = true,
    background = BackgroundColors(
        screen = Color(0xFFFFFFFF),
        secondary = Color(0xFF797979),
        toast = Color(0xFF000000),
    ),
    text = TextColors(
        primary = Color(0xFF000000),
        primaryDisabled = Color(0xFF000000).copy(alpha = 0.4f),
        secondary = Color(0xFF797979),
        secondaryDisabled = Color(0xFF797979).copy(alpha = 0.4f),
        invert = Color(0xFFFFFFFF),
        invertDisabled = Color(0xFFFFFFFF).copy(alpha = 0.4f),
        warning = Color(0xFFFAA105),
        error = Color(0xFFB00020),
        positive = Color(0xFF4CAF50)
    ),
    icon = IconColors(
        primary = Color(0xFF000000),
        primaryDisabled = Color(0xFF000000).copy(alpha = 0.4f),
        secondary = Color(0xFF797979),
        invert = Color(0xFFFFFFFF),
        warning = Color(0xFFFAA105),
        error = Color(0xFFB00020)
    ),
    palette = PaletteColors(
        white = Color(0xFFFFFFFF),
        white50 = Color(0x80FFFFFF),
        white10 = Color(0x1AFFFFFF),
        black = Color(0xFF000000),
        black50 = Color(0x80000000),
        black10 = Color(0x1A000000),
        grayscale = ExtendedPaletteColor(
            l900 = Color(0xFF151515)
        )
    ),
    button = ButtonColors(
        primary = Color(0xFF6750A4),
        primaryDisabled = Color(0xFF6750A4).copy(alpha = 0.4f),
        secondary = Color(0xFFFFFFFF),
        secondaryDisabled = Color(0xFFFFFFFF).copy(alpha = 0.4f)
    ),
    border = BorderColors(
        primary = Color(0xFF000000),
        error = Color(0xFFB00020)
    ),
    textField = TextFieldColors(
        background = Color(0xFFF2EBE3),
        backgroundDisabled = Color(0xFFF2EBE3).copy(alpha = 0.4f)
    ),
    common = CommonColors(
        positive = Color(0xFF00FF00),
        negative = Color(0xFFFF0000)
    )
)

val DarkAppColors = CustomColors(
    isLight = false,
    background = BackgroundColors(
        screen = Color(0xFF151515),
        secondary = Color(0xFFBDBDBD),
        toast = Color(0xFFFFFFFF),
    ),
    text = TextColors(
        primary = Color(0xFFFFFFFF),
        primaryDisabled = Color(0xFFFFFFFF).copy(alpha = 0.4f),
        secondary = Color(0xFFBDBDBD),
        secondaryDisabled = Color(0xFFBDBDBD).copy(alpha = 0.4f),
        invert = Color(0xFF000000),
        invertDisabled = Color(0xFF000000).copy(alpha = 0.4f),
        warning = Color(0xFFFAA105),
        error = Color(0xFFCF6679),
        positive = Color(0xFF81C784)
    ),
    icon = IconColors(
        primary = Color(0xFFFFFFFF),
        primaryDisabled = Color(0xFFFFFFFF).copy(alpha = 0.4f),
        secondary = Color(0xFFBDBDBD),
        invert = Color(0xFF000000),
        warning = Color(0xFFFAA105),
        error = Color(0xFFCF6679)
    ),
    palette = LightAppColors.palette,
    button = ButtonColors(
        primary = Color(0xFFBB86FC),
        primaryDisabled = Color(0xFFBB86FC).copy(alpha = 0.4f),
        secondary = Color(0xFF000000),
        secondaryDisabled = Color(0xFF000000).copy(alpha = 0.4f)
    ),
    border = BorderColors(
        primary = Color(0xFFFFFFFF),
        error = Color(0xFFCF6679)
    ),
    textField = TextFieldColors(
        background = Color(0xFF2E2E2E),
        backgroundDisabled = Color(0xFF2E2E2E).copy(alpha = 0.4f)
    ),
    common = CommonColors(
        positive = Color(0xFF00CC00),
        negative = Color(0xFFCC3333)
    )
)
