package io.github.drp08.studypal.presentation.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.drp08.studypal.domain.entities.SessionEntity

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject


@HiltViewModel
class WeeklyViewModel @Inject constructor(): ViewModel() {
    private val _currentDate =
        MutableStateFlow(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
    val currentDate: StateFlow<LocalDate> = _currentDate

    private val activities = mutableMapOf<DayOfWeek, List<SessionEntity>>()

    init {
        // Sample data
        activities[DayOfWeek.MONDAY] = listOf(
            SessionEntity( "Meeting", 32400, 36000), // 9:00 - 10:00
            SessionEntity( "Gym", 64800, 68400)      // 18:00 - 19:00
        )
        activities[DayOfWeek.TUESDAY] = listOf(
            SessionEntity( "Lunch", 43200, 46800),   // 12:00 - 13:00
            SessionEntity( "Study group", 50400, 57600) // 14:00 - 16:00
        )
        activities[DayOfWeek.WEDNESDAY] = listOf(
            SessionEntity( "Appointment", 36000, 39600) // 10:00 - 11:00
        )
        activities[DayOfWeek.THURSDAY] = listOf(
            SessionEntity( "Conference", 32400, 39600)  // 9:00 - 11:00
        )
        activities[DayOfWeek.FRIDAY] = listOf(
            SessionEntity( "Presentation", 46800, 50400) // 13:00 - 14:00
        )
    }

    fun getActivitiesForDay(dayOfWeek: DayOfWeek, currentDate: LocalDate): List<SessionEntity> {
        return activities[dayOfWeek] ?: emptyList()
    }
}