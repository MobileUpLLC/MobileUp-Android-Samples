package ru.mobileup.samples.features.work_manager.presentation.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.utils.SystemBarIconsColor
import ru.mobileup.samples.core.utils.SystemBars
import ru.mobileup.samples.features.R
import ru.mobileup.samples.core.R as CoreR

@Composable
fun WorkSampleInfoDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = showDialog,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        SystemBars(
            statusBarColor = Color.Transparent,
            navigationBarColor = Color.Transparent,
            statusBarIconsColor = SystemBarIconsColor.Light,
            navigationBarIconsColor = SystemBarIconsColor.Light
        )

        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .pointerInput(Unit) {
                    detectTapGestures { onDismiss() }
                },
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .widthIn(min = 280.dp, max = 560.dp),
                shape = RoundedCornerShape(28.dp),
                tonalElevation = 6.dp,
            ) {
                Column(
                    modifier = Modifier.padding(
                        start = 24.dp,
                        end = 24.dp,
                        top = 24.dp,
                        bottom = 16.dp
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = stringResource(R.string.work_info_description))
                    TextButton(
                        modifier = Modifier.align(Alignment.End),
                        onClick = onDismiss
                    ) {
                        Text(stringResource(CoreR.string.common_ok))
                    }
                }
            }
        }
    }
}
