package ru.mobileup.samples.features.calendar.presentation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import ru.mobileup.samples.features.calendar.domain.CalendarDay
import ru.mobileup.samples.features.calendar.domain.CalendarEvent
import ru.mobileup.samples.features.calendar.domain.CalendarPeriod
import java.time.LocalDate

class RealCalendarComponent(
    componentContext: ComponentContext,
) : ComponentContext by componentContext, CalendarComponent {
    override val selectedCalendarPeriod = MutableStateFlow(CalendarPeriod.Month)
    override val selectedCalendarDay = MutableStateFlow<CalendarDay?>(null)

    override val calendarEvents = MutableStateFlow(CalendarEvent.MOCK_LIST)

    override fun changeCalendarPeriod(period: CalendarPeriod) {
        selectedCalendarPeriod.value = period
    }

    override fun onCalendarDateClick(date: LocalDate) {
        selectedCalendarDay.value = if (date == selectedCalendarDay.value?.date) {
            null
        } else {
            CalendarDay(
                date = date,
                events = calendarEvents.value.filter { it.date == date }
            )
        }
    }
}
