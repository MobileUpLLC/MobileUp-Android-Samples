package ru.mobileup.samples.features.calendar.presentation.wigets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.ContentHeightMode
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import kotlinx.datetime.DayOfWeek
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.widget.button.AppButton
import ru.mobileup.samples.core.widget.button.ButtonType
import ru.mobileup.samples.features.R
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun MonthCalendarContent(modifier: Modifier = Modifier) {
    var selectCalendarType by remember { mutableStateOf(MonthCalendarType.Horizontal) }
    var selectCalendarTheme by remember { mutableStateOf(CalendarTheme.Default) }

    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) }
    val endMonth = remember { currentMonth.plusMonths(100) }
    val daysOfWeek = remember { daysOfWeek() }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first(),
    )

    Column(
        modifier = modifier.padding(vertical = 32.dp, horizontal = 16.dp)
    ) {
        val colors = selectCalendarTheme.colors()

        Spacer(modifier = Modifier.height(16.dp))
        when (selectCalendarType) {
            MonthCalendarType.Horizontal -> {
                HorizontalCalendar(
                    modifier = Modifier.height(300.dp),
                    state = state,
                    dayContent = {
                        Day(day = it)
                    },
                    monthHeader = { calendarMonth ->
                        MonthHeader(
                            modifier = Modifier.background(color = colors.second),
                            yearMonth = calendarMonth.yearMonth,
                            daysOfWeek = daysOfWeek
                        )
                    },
                    calendarScrollPaged = true,
                    contentHeightMode = ContentHeightMode.Fill,
                    monthBody = { _, content ->
                        Box(
                            modifier = Modifier.background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        colors.first,
                                        colors.second
                                    )
                                )
                            )
                        ) { content() }
                    },
                )
            }

            MonthCalendarType.Vertical -> {
                VerticalCalendar(
                    modifier = Modifier.height(300.dp),
                    state = state,
                    dayContent = { Day(day = it) },
                    monthHeader = { calendarMonth ->
                        MonthHeader(
                            modifier = Modifier.background(color = colors.second),
                            yearMonth = calendarMonth.yearMonth,
                            daysOfWeek = daysOfWeek
                        )
                    },
                    calendarScrollPaged = true,
                    contentHeightMode = ContentHeightMode.Fill,
                    monthBody = { _, content ->
                        Box(
                            modifier = Modifier.background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        colors.first,
                                        colors.second
                                    )
                                )
                            )
                        ) { content() }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        CalendarThemeButtons(
            selectCalendarTheme = selectCalendarTheme,
            onClick = { selectCalendarTheme = it }
        )

        Spacer(modifier = Modifier.height(8.dp))
        CalendarTypeButtons(
            selectCalendarType = selectCalendarType,
            onClick = { selectCalendarType = it }
        )
    }
}

@Composable
private fun MonthHeader(
    yearMonth: YearMonth,
    daysOfWeek: List<DayOfWeek>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(
                R.string.calendar_month_title,
                yearMonth.month.name,
                yearMonth.year.toString()
            ),
            color = CustomTheme.colors.text.primary,
            style = CustomTheme.typography.title.regular,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))
        DaysOfWeekTitle(daysOfWeek = daysOfWeek)
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
private fun DaysOfWeekTitle(
    daysOfWeek: List<DayOfWeek>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
            )
        }
    }
}

@Composable
private fun Day(
    day: CalendarDay,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = if (day.position == DayPosition.MonthDate) {
                CustomTheme.colors.text.primary
            } else {
                CustomTheme.colors.text.secondary
            }
        )
    }
}

@Composable
private fun CalendarThemeButtons(
    selectCalendarTheme: CalendarTheme,
    onClick: (CalendarTheme) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(
                color = CustomTheme.colors.background.secondary.copy(alpha = 0.2f),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CalendarTheme.entries.forEach { colorTheme ->
            Spacer(modifier = Modifier.width(4.dp))
            ColorButton(
                colors = colorTheme.colors(),
                isSelected = selectCalendarTheme == colorTheme,
                onClick = { onClick(colorTheme) }
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Composable
private fun ColorButton(
    colors: Pair<Color, Color>,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val arcAlpha = if (isSelected) 1f else 0.6f

    Box(
        modifier = modifier
            .size(36.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawArc(
                color = colors.first.copy(alpha = arcAlpha),
                startAngle = 90f,
                sweepAngle = 180f,
                useCenter = true,
                size = Size(width = 100f, height = 100f),
                topLeft = Offset.Zero,
            )

            drawArc(
                color = colors.second.copy(alpha = arcAlpha),
                startAngle = -90f,
                sweepAngle = 180f,
                useCenter = true,
                size = Size(width = 100f, height = 100f),
                topLeft = Offset.Zero,
            )
        }

        if (isSelected) {
            Icon(
                imageVector = Icons.Filled.Done,
                tint = CustomTheme.colors.icon.primary,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun CalendarTypeButtons(
    selectCalendarType: MonthCalendarType,
    onClick: (MonthCalendarType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        AppButton(
            modifier = Modifier.weight(1f),
            text = MonthCalendarType.Horizontal.name,
            buttonType = if (selectCalendarType == MonthCalendarType.Horizontal) {
                ButtonType.Primary
            } else {
                ButtonType.Secondary
            },
            onClick = { onClick(MonthCalendarType.Horizontal) },

            )

        Spacer(modifier = Modifier.width(16.dp))
        AppButton(
            modifier = Modifier.weight(1f),
            text = MonthCalendarType.Vertical.name,
            buttonType = if (selectCalendarType == MonthCalendarType.Vertical) {
                ButtonType.Primary
            } else {
                ButtonType.Secondary
            },
            onClick = { onClick(MonthCalendarType.Vertical) },
        )
    }
}

private enum class MonthCalendarType {
    Horizontal, Vertical
}

private enum class CalendarTheme {
    Default, RedGold, PurpleGold
}

@Composable
private fun CalendarTheme.colors(): Pair<Color, Color> {
    return when (this) {
        CalendarTheme.Default -> {
            CustomTheme.colors.icon.invert to CustomTheme.colors.icon.invert
        }

        CalendarTheme.PurpleGold -> {
            CustomTheme.colors.button.primary to CustomTheme.colors.icon.warning
        }

        CalendarTheme.RedGold -> {
            CustomTheme.colors.icon.warning to CustomTheme.colors.icon.error
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        MonthCalendarContent()
    }
}
