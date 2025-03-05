package ru.mobileup.samples.features.calendar.domain

import java.time.LocalDate

data class CalendarEvent(
    val name: String,
    val description: String?,
    val date: LocalDate,
    val type: EvenType
) {
    companion object {
        val MOCK_LIST = listOf(
            CalendarEvent(
                name = "Список покупок",
                description = "Хлеб, молоко, яйца, курица",
                date = LocalDate.now(),
                type = EvenType.Note
            ),
            CalendarEvent(
                name = "Сегодня доставка курьером",
                description = "Код на получение GDYU43545",
                date = LocalDate.now(),
                type = EvenType.Message
            ),
            CalendarEvent(
                name = "Зарепортить время в Jira",
                description = "Наконец-то принять приглашение и начать репортить время вовремя",
                date = LocalDate.now(),
                type = EvenType.Work
            ),
            CalendarEvent(
                name = "Отдать долг Игорю",
                description = null,
                date = LocalDate.now().minusDays(1),
                type = EvenType.Note
            ),
            CalendarEvent(
                name = "ОПЛАТИТЬ КОМУНАЛКУ",
                description = null,
                date = LocalDate.now().minusDays(1),
                type = EvenType.Message
            ),
            CalendarEvent(
                name = "Подготовить заявление на отпуск",
                description = "Заполнить таблицу, написать заявление и отправить на Наталью Отпуск",
                date = LocalDate.now().plusDays(3),
                type = EvenType.Work
            )
        )
    }

    enum class EvenType {
        Note, Message, Work
    }
}
