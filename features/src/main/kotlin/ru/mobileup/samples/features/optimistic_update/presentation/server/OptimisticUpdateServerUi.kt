package ru.mobileup.samples.features.optimistic_update.presentation.server

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.widget.PullRefreshLceWidget
import ru.mobileup.samples.core.widget.button.AppButton
import ru.mobileup.samples.core.widget.button.ButtonType
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.optimistic_update.domain.PaletteRequest

@Composable
fun OptimisticUpdateServerUi(
    component: OptimisticUpdateServerComponent,
    modifier: Modifier = Modifier
) {
    val requestsState by component.requests.collectAsState()

    Column(
        modifier = modifier.padding(20.dp)
    ) {
        Text(
            text = stringResource(R.string.optimistic_update_server_title),
            textAlign = TextAlign.Center,
            style = CustomTheme.typography.title.regular,
            color = CustomTheme.colors.text.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        )

        PullRefreshLceWidget(
            state = requestsState,
            onRefresh = component::onRefresh,
            onRetryClick = component::onRefresh,
        ) { data, _ ->
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(
                    items = data
                ) { index, item ->
                    RequestCard(
                        request = item,
                        onAcceptClick = { component.onAcceptClick(index) },
                        onFailClick = { component.onFailClick(index) },
                        onEndClick = { component.onEndClick(index) },
                    )
                }
            }
        }
    }
}

@Composable
private fun RequestCard(
    request: PaletteRequest,
    onAcceptClick: () -> Unit,
    onEndClick: () -> Unit,
    onFailClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shadowElevation = 12.dp,
        shape = RoundedCornerShape(12.dp),
        color = CustomTheme.colors.background.screen
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = stringResource(
                    when (request) {
                        is PaletteRequest.AddRequest -> R.string.optimistic_update_server_add_request
                        is PaletteRequest.RemoveRequest -> R.string.optimistic_update_server_remove_request
                    }
                ),
                style = CustomTheme.typography.caption.regular,
            )
            Box(
                modifier = Modifier
                    .height(20.dp)
                    .fillMaxWidth()
                    .background(Color(request.color.value))
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                AppButton(
                    onClick = onAcceptClick,
                    buttonType = ButtonType.Primary,
                    text = stringResource(
                        when (request.canBeAccepted) {
                            true -> R.string.optimistic_update_server_accept_request
                            false -> R.string.optimistic_update_server_accepted_request
                        }
                    ),
                    modifier = Modifier.weight(1f),
                    isEnabled = request.canBeAccepted
                )
                AppButton(
                    onClick = onEndClick,
                    buttonType = ButtonType.Secondary,
                    isEnabled = !request.canBeAccepted,
                    text = stringResource(R.string.optimistic_update_server_end_request),
                    modifier = Modifier.weight(1f)
                )
                AppButton(
                    onClick = onFailClick,
                    buttonType = ButtonType.Secondary,
                    text = stringResource(R.string.optimistic_update_server_fail_request)
                )
            }
        }
    }
}

@Preview
@Composable
private fun OptimisticUpdateServerUiPreview() {
    AppTheme {
        OptimisticUpdateServerUi(FakeOptimisticUpdateServerComponent())
    }
}
