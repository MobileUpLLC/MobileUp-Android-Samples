package ru.mobileup.samples.core.timer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.mobileup.samples.core.utils.timeNow
import kotlin.time.Duration.Companion.seconds

sealed interface TimerState {
    data class CountingDown(val time: Long) : TimerState
    data object Idle : TimerState
}

class Timer(private val coroutineScope: CoroutineScope) {

    private var timerJob: Job? = null

    private val _timerState = MutableStateFlow<TimerState>(TimerState.Idle)
    val timerState = _timerState.asStateFlow()

    fun start(timerTimeInSeconds: Long) {
        cancel()
        timerJob = coroutineScope.launch {
            val timeInTheFuture = timeNow().epochSeconds + timerTimeInSeconds
            var tick = timerTimeInSeconds
            while (tick != 0L) {
                tick = (timeInTheFuture - timeNow().epochSeconds).coerceAtLeast(0)
                _timerState.value = TimerState.CountingDown(tick)
                delay(1.seconds)
            }
        }.apply {
            invokeOnCompletion {
                _timerState.value = TimerState.Idle
            }
        }
    }

    fun cancel() {
        timerJob?.cancel()
        timerJob = null
    }
}