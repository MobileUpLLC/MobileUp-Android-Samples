package ru.mobileup.samples.features.pin_code.presentation.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.features.pin_code.domain.PinCodeProgressState

@Composable
fun PinCodeContent(
    pinProgressState: PinCodeProgressState,
    headerText: String,
    headerTextStyle: TextStyle,
    modifier: Modifier = Modifier,
    onDotsAnimationEnd: (() -> Unit)? = null
) {
    Column(modifier = modifier) {
        Text(
            text = headerText,
            style = headerTextStyle,
            color = CustomTheme.colors.text.primary,
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    top = 24.dp,
                    end = 16.dp,
                    bottom = 8.dp
                )
        )

        PinCodeProgress(
            pinProgressState = pinProgressState,
            onDotsAnimationEnd = onDotsAnimationEnd,
            modifier = Modifier
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 50.dp
                )
        )
    }
}
