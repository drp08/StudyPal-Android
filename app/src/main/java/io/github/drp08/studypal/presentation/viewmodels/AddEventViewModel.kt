package io.github.drp08.studypal.presentation.viewmodels

import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddEventViewModel : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    fun on(action: UiAction) {
        when (action) {
            is UiAction.ChangeSubject -> {
                _state.value = state.value.copy(subjectName = action.subjectName)
            }
            is UiAction.ChangeExamDate -> {
                _state.value = state.value.copy(examEpoch = action.examEpoch)
            }
            is UiAction.ChangeStudyHours -> {
                _state.value = state.value.copy(studyHours = action.studyHours)
            }
            is UiAction.ChangeConfidence -> {
                _state.value = state.value.copy(confidence = action.confidence)
            }
        }
    }

    data class State(
        val subjectName: String = "",
        val examEpoch: Long = System.currentTimeMillis(),
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