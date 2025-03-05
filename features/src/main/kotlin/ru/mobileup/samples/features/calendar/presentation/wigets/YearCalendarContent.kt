package ru.mobileup.samples.features.calendar.presentation.wigets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalYearCalendar
import com.kizitonwose.calendar.compose.yearcalendar.YearContentHeightMode
import com.kizitonwose.calendar.compose.yearcalendar.rememberYearCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.ExperimentalCalendarApi
import com.kizitonwose.calendar.core.daysOfWeek
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import java.time.Year
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalCalendarApi::class)
@Composable
fun YearCalendarContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        val currentYear = remember { Year.now() }
        val startYear = remember { currentYear.minusYears(50) }
        val endYear = remember { currentYear.plusYears(50) }
        val daysOfWeek = remember { daysOfWeek() }

        val state = rememberYearCalendarState(
            startYear = startYear,
            endYear = endYear,
            firstVisibleYear = currentYear,
            firstDayOfWeek = daysOfWeek.first(),
        )
        HorizontalYearCalendar(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            state = state,
            monthColumns = MONTH_COLUMNS,
            dayContent = { Day(day = it) },
            contentHeightMode = YearContentHeightMode.Fill,
            monthHorizontalSpacing = 52.dp,
            monthVerticalSpacing = 4.dp,
            yearBodyContentPadding = PaddingValues(all = 10.dp),
            monthHeader = { MonthHeader(calendarMonth = it) },
            yearHeader = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = it.year.value.toString(),
                    color = CustomTheme.colors.text.primary,
                    style = CustomTheme.typography.title.regular,
                    textAlign = TextAlign.Center
                )
            }
        )
    }
}

@Composable
private fun MonthHeader(
    calendarMonth: CalendarMonth,
    modifier: Modifier = Modifier,
) {
    val daysOfWeek = calendarMonth.weekDays.first().map { it.date.dayOfWeek }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = calendarMonth.yearMonth.month.name,
            color = CustomTheme.colors.text.primary,
            style = CustomTheme.typography.caption.regular,
            textAlign = TextAlign.Center
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            daysOfWeek.forEach { dayOfWeek ->
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    text = dayOfWeek
                        .getDisplayName(TextStyle.SHORT, Locale.getDefault()).first().toString(),
                    color = CustomTheme.colors.text.primary,
                    style = CustomTheme.typography.caption.small,
                )
            }
        }
    }
}

@Composable
private fun Day(day: CalendarDay) {
    Box(
        modifier = Modifier.aspectRatio(1f),
        contentAlignment = Alignment.Center,
    ) {
        if (day.position == DayPosition.MonthDate) {
            Text(
                text = day.date.dayOfMonth.toString(),
                color = CustomTheme.colors.text.primary,
                style = CustomTheme.typography.caption.small,
            )
        }
    }
}

private const val MONTH_COLUMNS = 3

@Preview
@Composable
private fun Preview() {
    AppTheme {
        YearCalendarContent()
    }
}
