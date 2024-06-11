package io.github.drp08.studypal.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import javax.inject.Inject
import kotlin.collections.MutableList

data class Event(val startTime: LocalTime, val endTime: LocalTime, val title: String)

@HiltViewModel
class DailyCalendarView @Inject constructor() : ViewModel() {
    private val _currentDate = MutableStateFlow(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
    val currentDate = _currentDate.asStateFlow()

    fun setCurrentDate(date: LocalDate) {
        viewModelScope.launch {
            _currentDate.value = date
        }
    }

    private val defaultEvents = listOf(
        Event(LocalTime(8, 30), LocalTime(9, 0), "Meeting 1"),
        Event(LocalTime(11, 0), LocalTime(12, 30), "Lunch"),
        Event(LocalTime(13, 0), LocalTime(15, 0), "Meeting 2"),
        Event(LocalTime(16, 0,0), LocalTime(17, 30), "Workshop"),
    )

    private val _events = MutableStateFlow(emptyList<Event>())
    val events = _events.asStateFlow()

    init {
        viewModelScope.launch {
            _events.value = defaultEvents
        }
    }

    fun addEvent(event: Event) {
        viewModelScope.launch {
            _events.value = events.toMutableList() + event
        }
    }

    private fun <T> StateFlow<List<T>>.toMutableList(): MutableList<T> {
        val currentValue = value
        return currentValue.toMutableList()
    }


}