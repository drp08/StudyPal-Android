package io.github.drp08.studypal.presentation.viewmodels

import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.navigator.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.drp08.studypal.db.daos.SubjectDao
import io.github.drp08.studypal.db.daos.TopicDao
import io.github.drp08.studypal.domain.SchedulingRepository
import io.github.drp08.studypal.domain.entities.SubjectEntity
import io.github.drp08.studypal.domain.entities.TopicEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddSubjectViewModel @Inject constructor(
    private val subjectDao: SubjectDao,
    private val topicDao: TopicDao,
    private val schedulingRepository: SchedulingRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SubjectEntity(
        name = "",
        confidenceLevel = 0f,
        totalSessions = 0,
        completedSessions = 0,
        scheduledSessions = 0,
        hoursPerWeek = 0,
        examEpoch = null
    ))
    val state = _state.asStateFlow()

    fun on(action: UiAction) {
        when (action) {
            is UiAction.ChangeSubject -> {
                _state.value = state.value.copy(name = action.subjectName)
            }
            is UiAction.ChangeExamDate -> {
                _state.value = state.value.copy(examEpoch = action.examEpoch)
            }
            is UiAction.ChangeStudyHours -> {
                _state.value = state.value.copy(hoursPerWeek = action.studyHours)
            }
            is UiAction.ChangeConfidence -> {
                _state.value = state.value.copy(confidenceLevel = action.confidence)
            }
            is UiAction.AddSubject -> {
                viewModelScope.launch {
                    subjectDao.upsertSubject(state.value)
                    for (i in 1..3) {
                        topicDao.upsertTopic(TopicEntity(
                            name = "Topic $i",
                            subject = state.value.name
                        ))
                    }
                    schedulingRepository.rescheduleAllSessions().collectLatest {
                        if (it)
                            action.navigator.pop()
                    }
                }
            }
        }
    }

    sealed class UiAction {
        data class ChangeSubject(val subjectName: String) : UiAction()
        data class ChangeExamDate(val examEpoch: Long) : UiAction()
        data class ChangeStudyHours(@IntRange(from = 1, to = 9) val studyHours: Int) : UiAction()
        data class ChangeConfidence(@FloatRange(from = 0.0, to = 1.0) val confidence: Float) : UiAction()
        data class AddSubject(val navigator: Navigator) : UiAction()
    }
}