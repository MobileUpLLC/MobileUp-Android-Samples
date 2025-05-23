package ru.mobileup.samples.features.chat.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import ru.mobileup.kmm_form_validation.control.InputControl
import ru.mobileup.samples.core.dialog.standard.StandardDialog
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.utils.SystemBarIconsColor
import ru.mobileup.samples.core.utils.SystemBars
import ru.mobileup.samples.core.widget.button.AppButton
import ru.mobileup.samples.core.widget.button.ButtonType
import ru.mobileup.samples.core.widget.text_field.AppTextField
import ru.mobileup.samples.core.widget.text_field.AppTextFieldDefaults
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.chat.domain.state.message.ChatMessage
import ru.mobileup.samples.features.chat.domain.state.message.ChatMessageId
import ru.mobileup.samples.features.chat.presentation.widget.AnimatedListItem
import ru.mobileup.samples.features.chat.presentation.widget.ChatMessageItem
import ru.mobileup.samples.features.chat.presentation.widget.DateDivider
import java.time.LocalDate

private val SUPPORTED_MIME_TYPES = arrayOf("image/*", "video/*", "application/pdf")

sealed class ChatListItem {
    data class MessageItem(val message: ChatMessage) : ChatListItem()
    data class DateDivider(val date: LocalDate) : ChatListItem()
}

@Composable
fun ChatUi(
    component: ChatComponent,
    modifier: Modifier = Modifier,
) {
    val changeSystemBarColor = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT

    if (changeSystemBarColor) {
        SystemBars(
            statusBarColor = CustomTheme.colors.chat.primary,
            navigationBarColor = CustomTheme.colors.chat.primary,
            statusBarIconsColor = SystemBarIconsColor.Light,
            navigationBarIconsColor = SystemBarIconsColor.Light
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            Toolbar()
        }
    ) { paddingValues ->
        ChatComponent(
            component = component,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun Toolbar(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(CustomTheme.colors.chat.primary)
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 24.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_app_logo),
            contentDescription = "logo",
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = stringResource(R.string.chat_header),
            color = CustomTheme.colors.palette.white,
            modifier = Modifier
                .weight(2f)
                .align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun ChatComponent(
    component: ChatComponent,
    modifier: Modifier = Modifier
) {
    val chatState = component.chatState.collectAsState()

    val state = rememberLazyListState()
    val isFloatingButtonVisible by remember(state) {
        derivedStateOf { state.firstVisibleItemIndex > 1 }
    }

    val isInProgress by remember(chatState) {
        derivedStateOf {
            chatState.value.historyLoadingState.inProgress
                    && chatState.value.chatMessagesState.messages.isEmpty()
        }
    }

    val isLoadFailed by remember(chatState) {
        derivedStateOf { chatState.value.historyLoadingState.error != null }
    }

    LaunchedEffect(Unit) {
        component.scrollToEnd.collect {
            if (!state.canScrollBackward) {
                state.requestScrollToItem(0)
            }
        }
    }

    Box(
        modifier = modifier
            .imePadding()
            .background(CustomTheme.colors.chat.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            when {
                isInProgress -> LoaderContent()

                isLoadFailed -> ErrorContent {
                    component.onReloadClick()
                }

                else -> {
                    val chatItems by remember(chatState.value.chatMessagesState) {
                        derivedStateOf {
                            groupMessagesByDate(chatState.value.chatMessagesState.allMessages)
                        }
                    }

                    MessageListContent(
                        state = state,
                        chatItems = chatItems,
                        onMessageClick = component::onMessageClick,
                        onScrolledToNotDownloadedImage = component::onScrolledToNotDownloadedImage
                    )
                }
            }

            InputLayout(
                inputControl = component.inputControl,
                onSendMessage = component::onSendMessage,
                onSendFile = component::onSendFile
            )
        }

        AnimatedVisibility(
            visible = isFloatingButtonVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 64.dp, end = 16.dp)
        ) {
            IconButton(
                onClick = {
                    state.requestScrollToItem(0)
                },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(CustomTheme.colors.chat.secondary)
                    .border(1.dp, CustomTheme.colors.chat.primary, CircleShape)
                    .padding(4.dp)
            ) {
                Icon(
                    modifier = Modifier,
                    painter = painterResource(R.drawable.ic_arrow_down),
                    tint = CustomTheme.colors.icon.primary,
                    contentDescription = null
                )
            }
        }

        StandardDialog(component.messageSendFailedDialog)
    }
}

@Composable
private fun ColumnScope.LoaderContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
    ) {
        CircularProgressIndicator(
            color = CustomTheme.colors.button.primary,
            strokeWidth = 4.dp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun ColumnScope.ErrorContent(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 32.dp)
        ) {
            Text(
                text = stringResource(R.string.chat_error_unknown),
                style = CustomTheme.typography.title.regular,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            AppButton(
                buttonType = ButtonType.Secondary,
                text = stringResource(R.string.chat_error_try_again),
                onClick = onClick,
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 32.dp)
            )
        }
    }
}

@Composable
private fun ColumnScope.MessageListContent(
    state: LazyListState,
    chatItems: List<ChatListItem>,
    onMessageClick: (ChatMessageId) -> Unit,
    onScrolledToNotDownloadedImage: (ChatMessageId) -> Unit
) {
    LazyColumn(state = state, modifier = Modifier.weight(1f), reverseLayout = true) {
        items(
            items = chatItems,
            key = { item ->
                when (item) {
                    is ChatListItem.DateDivider -> "divider_${item.date}"
                    is ChatListItem.MessageItem -> "message_${item.message.id.value}"
                }
            }
        ) { item ->
            when (item) {
                is ChatListItem.DateDivider -> DateDivider(item.date)
                is ChatListItem.MessageItem -> {
                    AnimatedListItem {
                        ChatMessageItem(
                            message = item.message,
                            onMessageClick = {
                                onMessageClick(item.message.id)
                            },
                            onScrolledToNotDownloadedImage = {
                                onScrolledToNotDownloadedImage(item.message.id)
                            },
                            modifier = it
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InputLayout(
    inputControl: InputControl,
    onSendMessage: () -> Unit,
    onSendFile: (Uri) -> Unit
) {
    val value = inputControl.value.collectAsState()

    val pickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                onSendFile(it)
            }
        }

    AppTextField(
        modifier = Modifier.fillMaxWidth(),
        inputControl = inputControl,
        placeholder = stringResource(R.string.chat_input_placeholder),
        colors = AppTextFieldDefaults.colors.copy(
            focusedContainerColor = CustomTheme.colors.chat.input,
            unfocusedContainerColor = CustomTheme.colors.chat.input,
            unfocusedTextColor = CustomTheme.colors.text.primary,
            focusedTextColor = CustomTheme.colors.text.primary
        ),
        visualTransformation = inputControl.visualTransformation,
        leadingIcon = {
            Icon(
                modifier = Modifier
                    .clickable {
                        pickerLauncher.launch(SUPPORTED_MIME_TYPES)
                    }
                    .padding(8.dp),
                painter = painterResource(
                    R.drawable.ic_attach
                ),
                tint = CustomTheme.colors.icon.primary,
                contentDescription = null
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = value.value.isNotEmpty(),
                enter = expandHorizontally(
                    expandFrom = Alignment.End
                ),
                exit = fadeOut()
            ) {
                Icon(
                    modifier = Modifier
                        .clickable {
                            onSendMessage()
                        }
                        .padding(8.dp),
                    painter = painterResource(
                        R.drawable.ic_send
                    ),
                    tint = CustomTheme.colors.icon.primary,
                    contentDescription = null
                )
            }
        },
        shape = RectangleShape,
        border = BorderStroke(2.dp, CustomTheme.colors.chat.tertiary)
    )
}

/**
 * Groups messages by date and creates a list of ChatListItems with date dividers
 */
private fun groupMessagesByDate(messages: List<ChatMessage>): List<ChatListItem> {
    if (messages.isEmpty()) return emptyList()

    val result = mutableListOf<ChatListItem>()
    var currentDate: LocalDate? = null

    messages.forEach { message ->
        val messageDate = message.time.toLocalDate()

        if (currentDate == null || !messageDate.isEqual(currentDate)) {
            currentDate = messageDate
            result.add(ChatListItem.DateDivider(messageDate))
        }

        result.add(ChatListItem.MessageItem(message))
    }

    return result.reversed()
}

@Preview
@Composable
private fun ChatUiPreview() {
    AppTheme {
        ChatUi(FakeChatComponent())
    }
}