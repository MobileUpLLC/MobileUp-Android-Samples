package ru.mobileup.samples.features.pin_code.presentation.create

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.dialog.standard.StandardDialog
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.pin_code.presentation.widget.KeyboardUi
import ru.mobileup.samples.features.pin_code.presentation.widget.PinCodeContent

@Composable
fun CreatePinCodeUi(
    component: CreatePinCodeComponent,
    modifier: Modifier = Modifier
) = Scaffold(
    modifier = modifier,
    content = {
        CreatePinCodeContent(
            component = component,
            modifier = Modifier.padding(it)
        )
    }
)

@Composable
fun CreatePinCodeContent(
    component: CreatePinCodeComponent,
    modifier: Modifier = Modifier,
) {
    val passcodeState by component.pinInputStep.collectAsState()
    val pinProgressState by component.pinProgressState.collectAsState()
    val isEraseButtonAvailable by component.isEraseButtonAvailable.collectAsState()

    Column(
        modifier = modifier
    ) {
        AnimatedContent(
            targetState = passcodeState,
            transitionSpec = {
                val animationSpec = tween<IntOffset>(
                    durationMillis = 500,
                    easing = LinearEasing
                )
                if (targetState == CreatePinCodeComponent.PinInputStep.Repeat) {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = animationSpec
                    ) togetherWith slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = animationSpec
                    )
                } else {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = animationSpec
                    ) togetherWith slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = animationSpec
                    )
                }
            }, label = ""
        ) { passcodeState ->
            PinCodeContent(
                pinProgressState = pinProgressState,
                headerText = when (passcodeState) {
                    CreatePinCodeComponent.PinInputStep.Repeat -> {
                        stringResource(id = R.string.pin_code_create_success)
                    }
                    CreatePinCodeComponent.PinInputStep.None -> {
                        stringResource(id = R.string.pin_code_create_header)
                    }
                    CreatePinCodeComponent.PinInputStep.PreviouslyErred -> {
                        stringResource(id = R.string.pin_code_create_error)
                    }
                },
                headerTextStyle = CustomTheme.typography.title.regular,
                onDotsAnimationEnd = component::onPinCodeInputAnimationEnd
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        KeyboardUi(
            modifier = Modifier
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    bottom = 82.dp
                ),
            onDigitClick = component::onDigitClick,
            extraEndBtn = {
                if (isEraseButtonAvailable) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = CustomTheme.colors.background.screen)
                            .clickable(
                                onClick = component::onEraseClick,
                                indication = ripple(bounded = true),
                                interactionSource = remember { MutableInteractionSource() }
                            )
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_24_cancel),
                            contentDescription = null,
                            tint = CustomTheme.colors.icon.secondary
                        )
                    }
                }
            }
        )

        StandardDialog(dialogControl = component.dialogControl)
    }
}

@Preview
@Composable
private fun CreatePinCodeUiPreview() {
    AppTheme {
        CreatePinCodeUi(component = FakeCreatePinCodeComponent())
    }
}
