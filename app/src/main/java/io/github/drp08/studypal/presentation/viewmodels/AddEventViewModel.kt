package io.github.drp08.studypal.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.navigator.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.drp08.studypal.db.daos.SessionDao
import io.github.drp08.studypal.domain.SchedulingRepository
import io.github.drp08.studypal.domain.entities.SessionEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class AddEventViewModel @Inject constructor(
    private val sessionDao: SessionDao,
    private val schedulingRepository: SchedulingRepository
) : ViewModel() {
    private val _state = MutableStateFlow(SessionEntity(
        sessionId = 0,
        parent = "",
        startTime = 0,
        endTime = 0
    ))

    val state = _state.asStateFlow()

    fun on(action: UiAction) {
        when (action) {
            is UiAction.ChangeEvent -> {
                _state.value = _state.value.copy(parent = action.eventName)
            }
            is UiAction.ChangeStartTime -> {
                _state.value = _state.value.copy(startTime = action.startTime)
            }
            is UiAction.ChangeEndTime -> {
                _state.value = _state.value.copy(endTime = action.endTime)
            }
            is UiAction.AddEvent -> {
                viewModelScope.launch {
                    sessionDao.upsertSession(state.value)
                    schedulingRepository.rescheduleAllSessions().collectLatest {
                        if (it)
                            action.navigator.pop()
                    }
                }
            }
        }
    }


    sealed class UiAction {
        data class ChangeEvent(val eventName: String) : UiAction()
        data class ChangeStartTime(val startTime: Long) : UiAction()
        data class ChangeEndTime(val endTime: Long) : UiAction()
        data class AddEvent(val navigator: Navigator) : UiAction()
    }
}