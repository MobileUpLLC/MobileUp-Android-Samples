package ru.mobileup.samples.features.pin_code.presentation.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme

@Composable
fun KeyboardUi(
    onDigitClick: (digit: Int) -> Unit,
    modifier: Modifier = Modifier,
    extraEndBtn: @Composable (() -> Unit)? = null,
    extraStartBtn: @Composable (() -> Unit)? = null,
) = Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        val digitModifier = Modifier
            .height(66.dp)
            .weight(1f)

        (1..9)
            .chunked(3)
            .forEach {
                KeyboardRow {
                    it.forEach { digit ->
                        DigitButton(
                            modifier = digitModifier,
                            digit = digit,
                            onClick = { onDigitClick(digit) }
                        )
                    }
                }
            }

        KeyboardRow {
            Box(
                modifier = digitModifier,
                contentAlignment = Alignment.Center
            ) {
                extraStartBtn?.invoke()
            }
            DigitButton(
                modifier = digitModifier,
                digit = 0,
                onClick = { onDigitClick(0) }
            )
            Box(
                modifier = digitModifier,
                contentAlignment = Alignment.Center
            ) {
                extraEndBtn?.invoke()
            }
        }
    }
}

@Composable
fun KeyboardRow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier
    ) {
        content()
    }
}

@Composable
fun DigitButton(
    digit: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) = Button(
    onClick = { onClick() },
    modifier = modifier,
    shape = RectangleShape,
    colors = ButtonDefaults.buttonColors(
        containerColor = Color.Transparent,
        contentColor = CustomTheme.colors.text.primary
    ),
    elevation = null
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.align(alignment = Alignment.Center),
            text = digit.toString(),
            style = CustomTheme.typography.title.regular
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun KeyboardUiPreview() {
    AppTheme {
        KeyboardUi(onDigitClick = {})
    }
}
