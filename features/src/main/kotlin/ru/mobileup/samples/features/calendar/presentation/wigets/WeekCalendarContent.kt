package ru.mobileup.samples.features.calendar.presentation.wigets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.yearMonth
import kotlinx.datetime.Month
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.utils.toDay
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.calendar.domain.CalendarDay
import ru.mobileup.samples.features.calendar.domain.CalendarEvent
import ru.mobileup.samples.features.calendar.domain.CalendarEvent.EvenType.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Suppress("ModifierNotUsedAtRoot")
@Composable
fun WeekCalendarContent(
    selectedCalendarDay: CalendarDay?,
    calendarEvents: List<CalendarEvent>,
    onClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentDate = remember { LocalDate.now() }
    val currentMonth = remember { YearMonth.now() }
    val startDate = remember { currentMonth.minusMonths(100).atStartOfMonth() }
    val endDate = remember { currentMonth.plusMonths(100).atEndOfMonth() }
    val daysOfWeek = remember { daysOfWeek() }

    val state = rememberWeekCalendarState(
        startDate = startDate,
        endDate = endDate,
        firstVisibleWeekDate = currentDate,
        firstDayOfWeek = daysOfWeek.first()
    )
    Column {
        WeekCalendar(
            modifier = modifier,
            state = state,
            dayContent = { day ->
                Day(
                    date = day.date,
                    isSelected = day.date == selectedCalendarDay?.date,
                    events = calendarEvents.filter { it.date == day.date },
                    onClick = { onClick(day.date) }
                )
            },
            weekHeader = {
                Text(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth(),
                    text = getWeekPageTitle(it),
                    color = CustomTheme.colors.text.primary,
                    style = CustomTheme.typography.title.regular,
                    textAlign = TextAlign.Center
                )
            }
        )

        Spacer(modifier = Modifier.height(32.dp))
        selectedCalendarDay?.events?.forEach { event ->
            CalendarEventCard(event = event)
        }
    }
}

@Composable
private fun Day(
    date: LocalDate,
    isSelected: Boolean,
    events: List<CalendarEvent>,
    onClick: (LocalDate) -> Unit
) {
    Box(
        modifier = Modifier
            .background(color = if (isSelected) CustomTheme.colors.background.secondary else Color.Transparent)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick(date) },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                color = if (isSelected) CustomTheme.colors.text.invert else CustomTheme.colors.text.primary,
                style = CustomTheme.typography.body.regular,
            )

            Text(
                text = date.toDay(),
                color = if (isSelected) CustomTheme.colors.text.invert else CustomTheme.colors.text.primary,
                style = CustomTheme.typography.body.regular,
            )
        }

        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            events.distinctBy { it.type }.forEach { event ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .background(
                            color = when (event.type) {
                                Note -> CustomTheme.colors.icon.primary
                                Message -> CustomTheme.colors.icon.warning
                                Work -> CustomTheme.colors.icon.error
                            }
                        )
                )
            }
        }
    }
}

@Composable
private fun CalendarEventCard(
    event: CalendarEvent,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .background(
                color = when (event.type) {
                    Note -> CustomTheme.colors.icon.primary
                    Message -> CustomTheme.colors.icon.warning
                    Work -> CustomTheme.colors.icon.error
                },
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = event.name,
            color = CustomTheme.colors.text.invert,
            style = CustomTheme.typography.body.regular,
        )

        if (event.description != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = event.description,
                color = CustomTheme.colors.text.invert,
                style = CustomTheme.typography.caption.regular,
            )
        }
    }
}

@Composable
private fun getWeekPageTitle(week: Week): String {
    val firstDate = week.days.first().date
    val lastDate = week.days.last().date

    return when {
        firstDate.yearMonth == lastDate.yearMonth -> firstDate.yearMonth.displayText()

        else -> stringResource(
            id = R.string.calendar_week_title,
            firstDate.yearMonth.displayText(),
            lastDate.yearMonth.displayText()
        )
    }
}

private fun YearMonth.displayText(short: Boolean = false): String {
    return "${month.displayText(short = short)} $year"
}

private fun Month.displayText(short: Boolean = false): String {
    return getDisplayName(if (short) TextStyle.SHORT else TextStyle.FULL, Locale.getDefault())
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        WeekCalendarContent(
            selectedCalendarDay = null,
            calendarEvents = CalendarEvent.MOCK_LIST,
            onClick = {}
        )
    }
}
