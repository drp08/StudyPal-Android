package io.github.drp08.studypal.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.drp08.studypal.domain.entities.SessionEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


class MonthlyCalendarViewModel : ViewModel() {
    private val _currentDate = MutableStateFlow(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
    val currentDate: StateFlow<LocalDate> = _currentDate.asStateFlow()

    private val _events = MutableStateFlow<List<SessionEntity>>(emptyList())
    val events: StateFlow<List<SessionEntity>> = _events.asStateFlow()

    fun setCurrentDate(date: LocalDate) {
        viewModelScope.launch {
            _currentDate.value = date
        }
    }

    fun addEvent(event: SessionEntity) {
        viewModelScope.launch {
            _events.value = _events.value.toMutableList().apply { add(event) }
        }
    }

    fun getEventsForDate(date: LocalDate): List<SessionEntity> {
        return _events.value.filter { event ->
            val eventStartDate = LocalDateTime(
                date.year, date.monthNumber, date.dayOfMonth,
                event.startTime / 3600, (event.startTime % 3600) / 60
            )
            val eventEndDate = LocalDateTime(
                date.year, date.monthNumber, date.dayOfMonth,
                event.endTime / 3600, (event.endTime % 3600) / 60
            )
            eventStartDate.date == date || eventEndDate.date == date || (eventStartDate.date < date && eventEndDate.date > date)
        }
    }
}