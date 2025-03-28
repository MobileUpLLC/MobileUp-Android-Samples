package ru.mobileup.samples.features.pin_code.presentation.check

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.utils.collectIsLongPressedAsState
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.pin_code.domain.PinCodeProgressState
import ru.mobileup.samples.features.pin_code.presentation.widget.KeyboardUi
import ru.mobileup.samples.features.pin_code.presentation.widget.PinCodeProgress
import ru.mobileup.samples.core.R as CoreR

@Composable
fun CheckPinCodeUi(
    component: CheckPinCodeComponent,
    modifier: Modifier = Modifier,
) {
    val pinProgressState by component.pinProgressState.collectAsState()
    val isBiometricsSupported by component.isBiometricsSupported.collectAsState()
    val isTimerDialogVisible by component.isTimerDialogVisible.collectAsState()
    val isLogoutDialogVisible by component.isLogoutDialogVisible.collectAsState()
    val isError by component.isError.collectAsState()

    CheckPinCodeContent(
        modifier = modifier,
        pinProgressState = pinProgressState,
        isBiometricsSupported = isBiometricsSupported,
        isTimerDialogVisible = isTimerDialogVisible,
        isLogoutDialogVisible = isLogoutDialogVisible,
        isError = isError,
        onLogoutDialogVisibilityChange = component::onLogoutDialogVisibilityChange,
        onLogoutConfirm = component::onLogoutConfirmed,
        onDialogDismiss = component::onDialogDismiss,
        onDigitClick = component::onDigitClick,
        onEraseClick = component::onEraseClick,
        onBiometricClick = component::onBiometricClick,
        onDotsAnimationEnd = component::onPinCodeInputAnimationEnd
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CheckPinCodeContent(
    pinProgressState: PinCodeProgressState,
    isBiometricsSupported: Boolean,
    isTimerDialogVisible: Boolean,
    isLogoutDialogVisible: Boolean,
    isError: Boolean,
    onDigitClick: (digit: Int) -> Unit,
    onEraseClick: () -> Unit,
    onBiometricClick: () -> Unit,
    onLogoutDialogVisibilityChange: () -> Unit,
    onLogoutConfirm: () -> Unit,
    onDialogDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    onDotsAnimationEnd: (() -> Unit)? = null,
) {
    val progressCounter = (pinProgressState as? PinCodeProgressState.Progress)?.count ?: 0
    val onRightBtnClickSource = remember { MutableInteractionSource() }
    val isRightBtnLongPressed by onRightBtnClickSource.collectIsLongPressedAsState()
    if (isRightBtnLongPressed && progressCounter > 0) {
        onEraseClick()
    }

    Column(
        modifier = modifier
            .background(CustomTheme.colors.background.screen)
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        AnimatedContent(
            targetState = isError,
            transitionSpec = {
                val animationSpec = tween<IntOffset>(
                    durationMillis = 500,
                    easing = LinearEasing
                )
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = animationSpec
                ) togetherWith slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = animationSpec
                )
            },
            modifier = Modifier.weight(1f), label = ""
        ) { isError ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 82.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = if (isError) {
                        stringResource(R.string.pin_code_check_error)
                    } else {
                        stringResource(R.string.pin_code_check_header)
                    },
                    style = CustomTheme.typography.title.regular
                )

                PinCodeProgress(
                    pinProgressState = pinProgressState,
                    onDotsAnimationEnd = onDotsAnimationEnd,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 50.dp)
                )
            }
        }

        KeyboardUi(
            modifier = Modifier.padding(bottom = 82.dp),
            onDigitClick = onDigitClick,
            extraStartBtn = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onLogoutDialogVisibilityChange() }
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.pin_code_keyboard_logout),
                        style = CustomTheme.typography.body.regular,
                        color = CustomTheme.colors.text.secondary
                    )
                }
            },
            extraEndBtn = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .combinedClickable(
                            enabled = progressCounter > 0 || isBiometricsSupported,
                            onClick = {
                                if (progressCounter > 0) {
                                    onEraseClick()
                                } else {
                                    onBiometricClick()
                                }
                            },
                            role = Role.Button,
                            interactionSource = onRightBtnClickSource,
                            indication = ripple(bounded = true)
                        )
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (progressCounter > 0 || !isBiometricsSupported) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_24_cancel),
                            contentDescription = null,
                            modifier = Modifier.alpha(if (progressCounter == 0) 0f else 1f),
                            tint = CustomTheme.colors.icon.secondary
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_24_touch),
                            contentDescription = null,
                            tint = CustomTheme.colors.icon.secondary
                        )
                    }
                }
            }
        )
    }

    if (isTimerDialogVisible) {
        AlertDialog(
            onDismissRequest = onDialogDismiss,
            confirmButton = {
                TextButton(
                    onClick = onDialogDismiss,
                    content = { Text(stringResource(CoreR.string.common_ok)) }
                )
            },
            title = { Text(stringResource(R.string.pin_code_alert_error_header)) },
            text = { Text(stringResource(R.string.pin_code_alert_error_text)) },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }

    if (isLogoutDialogVisible) {
        AlertDialog(
            onDismissRequest = onLogoutDialogVisibilityChange,
            confirmButton = {
                TextButton(
                    onClick = onLogoutConfirm,
                    content = { Text(stringResource(CoreR.string.common_yes)) }
                )
            },
            dismissButton = {
                TextButton(
                    onClick = onLogoutDialogVisibilityChange,
                    content = { Text(stringResource(CoreR.string.common_no)) }
                )
            },
            title = { Text(stringResource(R.string.pin_code_alert_logout_header)) },
            text = { Text(stringResource(R.string.pin_code_alert_logout_text)) }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CheckPinCodeUiPreview() {
    AppTheme { CheckPinCodeUi(FakeCheckPinCodeComponent()) }
}
