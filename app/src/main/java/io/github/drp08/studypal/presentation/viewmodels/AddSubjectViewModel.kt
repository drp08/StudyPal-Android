package io.github.drp08.studypal.presentation.viewmodels

import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.lifecycle.ViewModel
import io.github.drp08.studypal.domain.entities.SubjectEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class AddSubjectViewModel : ViewModel() {

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
        }
    }

    data class State(
        val subjectName: String = "",
        val examEpoch: LocalDate =
            Clock.System.now().toLocalDateTime(
                TimeZone.currentSystemDefault()).date,
        @IntRange(from = 1, to = 9)
        val studyHours: Int = 1,
        @FloatRange(from = 0.0, to = 1.0)
        val confidence: Float = 0f
    )

    sealed class UiAction {
        data class ChangeSubject(val subjectName: String) : UiAction()
        data class ChangeExamDate(val examEpoch: Long) : UiAction()
        data class ChangeStudyHours(@IntRange(from = 1, to = 9) val studyHours: Int) : UiAction()
        data class ChangeConfidence(@FloatRange(from = 0.0, to = 1.0) val confidence: Float) : UiAction()
    }
}