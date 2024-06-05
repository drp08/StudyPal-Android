package io.github.drp08.studypal.presentation.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import network.chaintech.utils.now

data class Activity(val title: String)
class WeeklyViewModel : ViewModel(){
    private val activities = mutableMapOf<LocalDate, List<Activity>>()

    private val _currentDate = MutableStateFlow(LocalDate.now())
    val currentDate: StateFlow<LocalDate> = _currentDate

    init {
        // Mock data for activities
        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)
        val dayAfterTomorrow = today.plusDays(2)

        activities[today] = listOf(Activity("Meeting"), Activity("Gym"))
        activities[tomorrow] = listOf(Activity("Lunch"), Activity("Study group"))
        activities[dayAfterTomorrow] = listOf(Activity("Appointment"))
    }

    fun getActivitiesForDay(day: DayOfWeek, currentDate: LocalDate): List<Activity> {
        val targetDate = currentDate.with(day)
        return activities[targetDate] ?: emptyList()
    }

    fun LocalDate.plusDays(days: Int): LocalDate {
        return this.plus(days, DateTimeUnit.DAY)
    }

    fun LocalDate.with(day: DayOfWeek): LocalDate {
        val currentDayOfWeek = this.dayOfWeek.ordinal
        val targetDayOfWeek = day.ordinal
        val difference = targetDayOfWeek - currentDayOfWeek
        return this.plusDays(difference)
    }
}