package io.github.drp08.studypal.presentation.viewmodels

import androidx.lifecycle.ViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import network.chaintech.utils.now

data class Activity(
    val title: String,
    val startTime: String,
    val endTime: String
)
class WeeklyViewModel : ViewModel(){
    private val _currentDate = MutableStateFlow(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
    val currentDate: StateFlow<LocalDate> = _currentDate

    private val activities = mutableMapOf<DayOfWeek, List<Activity>>()

    init {
        // Sample data
        activities[DayOfWeek.MONDAY] = listOf(
            Activity("Meeting", "9:00", "10:00"),
            Activity("Gym", "18:00", "19:00")
        )
        activities[DayOfWeek.TUESDAY] = listOf(
            Activity("Lunch", "12:00", "13:00"),
            Activity("Study group", "14:00", "16:00")
        )
        activities[DayOfWeek.WEDNESDAY] = listOf(
            Activity("Appointment", "10:00", "11:00")
        )
        activities[DayOfWeek.THURSDAY] = listOf(
            Activity("Conference", "9:00", "11:00")
        )
        activities[DayOfWeek.FRIDAY] = listOf(
            Activity("Presentation", "13:00", "14:00")
        )
    }

    fun getActivitiesForDay(dayOfWeek: DayOfWeek, currentDate: LocalDate): List<Activity> {
        return activities[dayOfWeek] ?: emptyList()
    }

    private fun LocalDate.plusDays(days: Int): LocalDate {
        return this.plus(days, DateTimeUnit.DAY)
    }
}