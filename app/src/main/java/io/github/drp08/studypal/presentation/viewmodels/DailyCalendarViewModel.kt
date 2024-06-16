package io.github.drp08.studypal.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.drp08.studypal.db.daos.SessionDao
import io.github.drp08.studypal.presentation.models.DailyViewEvent
import io.github.drp08.studypal.utils.toEpochMilliSecond
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toLocalDateTime
import network.chaintech.utils.now
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class DailyCalendarViewModel @Inject constructor(
    private val sessionDao: SessionDao
) : ViewModel() {
    private val _currentDate =
        MutableStateFlow(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
    val currentDate = _currentDate.asStateFlow()

    fun setCurrentDate(date: LocalDate) {
        viewModelScope.launch {
            _currentDate.value = date
        }
    }

    private val _events = MutableStateFlow(emptyList<DailyViewEvent>())
    val events = _events.asStateFlow()

    init {
        viewModelScope.launch {
            val startOfDay =
                LocalDate.now().toJavaLocalDate().atStartOfDay().atZone(ZoneId.of("UTC"))
                    .toEpochMilliSecond()

            sessionDao.getSessionsWithSubjectAndTopic().collectLatest { subjectTopicsSessions ->
                for ((subject, topicSessions) in subjectTopicsSessions) {
                    for ((_, sessions) in topicSessions) {
                        for (session in sessions) {
                            val dailyViewEvent = DailyViewEvent(
                                LocalTime.fromMillisecondOfDay((session.startTime - startOfDay).toInt()),
                                LocalTime.fromMillisecondOfDay((session.endTime - startOfDay).toInt()),
                                title = subject.name
                            )
                            _events.value = (events.value + dailyViewEvent).distinct()
                        }
                    }
                }
            }
        }
    }

    fun addEvent(event: DailyViewEvent) {
        viewModelScope.launch {
            _events.value = events.toMutableList() + event
        }
    }

    private fun <T> StateFlow<List<T>>.toMutableList(): MutableList<T> {
        val currentValue = value
        return currentValue.toMutableList()
    }
}