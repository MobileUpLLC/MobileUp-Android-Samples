package ru.mobileup.samples.features.collapsing_toolbar.presentation.widgets

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import ru.mobileup.samples.core.utils.dispatchOnBackPressed
import kotlin.math.roundToInt

private const val LEADING_ICON_ID = "SimpleCustomToolbarParams_LEADING_ICON_ID"
private const val TITLE_ID = "SimpleCustomToolbarParams_TITLE_ID"

private const val COLLAPSED_OFFSET = 1000
private const val MAX_CONTAINER_HEIGHT = 96f
private const val MIN_CONTAINER_HEIGHT = 44f
private const val LEADING_ICON_SIZE = 24
private const val TEXT_ICON_GAP = 8
private const val VERTICAL_PADDING = 8
private const val TITLE_PADDING_ACCELERATION = 2f

@Composable
fun CollapsingToolbarSimpleCustom(
    scrollValue: Int,
    modifier: Modifier = Modifier,
    title: String = "SimpleCustom",
    leadingIconImageVector: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
) {
    val ctx = LocalContext.current
    val limitedScrollValue =
        remember(scrollValue) { scrollValue.coerceAtMost(COLLAPSED_OFFSET).toFloat() }
    val scrollFraction = remember(limitedScrollValue) { limitedScrollValue / COLLAPSED_OFFSET }

    val containerHeight by animateFloatAsState(
        lerp(MAX_CONTAINER_HEIGHT, MIN_CONTAINER_HEIGHT, scrollFraction)
    )
    Layout(
        modifier = modifier
            .height(containerHeight.dp)
            .padding(horizontal = 16.dp)
            .drawWithContent {
                drawContent()
                drawLine(
                    color = Color.Black,
                    start = Offset(0f, size.height - 4 * density),
                    end = Offset(size.width, size.height - 4 * density)
                )
            },
        content = {
            Icon(
                imageVector = leadingIconImageVector,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(onClick = { dispatchOnBackPressed(ctx) })
                    .layoutId(LEADING_ICON_ID)
            )

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                modifier = Modifier.layoutId(TITLE_ID)
            )
        }
    ) { measures, constraints ->
        val verticalPadding = VERTICAL_PADDING * density

        //region icon
        val leadingIconSizeDp = LEADING_ICON_SIZE * density
        val leadingIconPlaceable = measures.find { it.layoutId == LEADING_ICON_ID }?.measure(
            Constraints(
                minWidth = leadingIconSizeDp.roundToInt(),
                minHeight = leadingIconSizeDp.roundToInt(),
            )
        )

        val leadingIconY =
            lerp(0, constraints.maxHeight / 2, scrollFraction).toFloat() + // скролл сверху к центру
                (verticalPadding * (1 - scrollFraction)) - // по мере скролла к центру убираем верхний отступ
                (leadingIconSizeDp / 2 * scrollFraction) // учитываем высоту иконки
        //endregion

        //region title
        val titlePlaceable = measures.find { it.layoutId == TITLE_ID }?.measure(
            Constraints(maxWidth = constraints.maxWidth - (TEXT_ICON_GAP * 2))
        )
        val titleHeight = titlePlaceable?.height?.toFloat() ?: 0f
        val leadingIconWidth = leadingIconPlaceable?.width?.toFloat() ?: 0f
        val titleExtraPaddingFromIcon = leadingIconWidth *
            (scrollFraction * TITLE_PADDING_ACCELERATION).coerceAtMost(1f)
        val titleX = titleExtraPaddingFromIcon + // добавляем ширину иконки с коэффициентом
            (TEXT_ICON_GAP * density * scrollFraction) // добавляем отступ от икони
        val titleY = lerp(constraints.maxHeight, constraints.maxHeight / 2, scrollFraction) - // скролл снизу к центруч
            (verticalPadding * (1 - scrollFraction)) - // по мере скролла к центру убираем нижний отступ
            (titleHeight - (titleHeight / 2 * scrollFraction)) // учитываем высоту текста

        //endregion

        layout(constraints.maxWidth, constraints.maxHeight) {
            leadingIconPlaceable?.place(
                x = 0,
                y = leadingIconY.roundToInt()
            )

            titlePlaceable?.place(
                x = titleX.roundToInt(),
                y = titleY.roundToInt()
            )
        }
    }
}
