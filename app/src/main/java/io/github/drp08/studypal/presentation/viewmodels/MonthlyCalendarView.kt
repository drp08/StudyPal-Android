package io.github.drp08.studypal.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class MonthlyCalendarView : ViewModel() {
    private val _currentDate = MutableStateFlow(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
    val currentDate: StateFlow<LocalDate> = _currentDate.asStateFlow()

    private val _events = MutableStateFlow<List<MonthlyEvent>>(emptyList())
    val events: StateFlow<List<MonthlyEvent>> = _events.asStateFlow()

    fun setCurrentDate(date: LocalDate) {
        viewModelScope.launch {
            _currentDate.value = date
        }
    }

    fun addEvent(event: MonthlyEvent) {
        viewModelScope.launch {
            _events.value = _events.value.toMutableList().apply { add(event) }
        }
    }

    fun getEventsForDate(date: LocalDate): List<MonthlyEvent> {
        return _events.value.filter { event ->
            event.startTime.date == date || event.endTime.date == date || (event.startTime.date < date && event.endTime.date > date)
        }
    }
}

data class MonthlyEvent(
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val title: String,
    val description: String? = null)