package io.github.drp08.studypal.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.drp08.studypal.domain.SchedulingRepository
import io.github.drp08.studypal.domain.models.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class HomeViewModel(
    private val schedulingRepository: SchedulingRepository
): ViewModel() {
    private val _sessions = MutableStateFlow(emptyList<Session>())
    val sessions = _sessions.asStateFlow()

    private val subjects = listOf("Chemistry", "Computer Science", "Mathematics", "Biology", "Physics",
        "Economics", "Accounting", "History", "Geography", "Sociology", "Literature", "Psychology")

    fun addNewSession() {
        viewModelScope.launch {
            val randomIndex = Random.nextInt(subjects.size)
            val randomSubject = subjects[randomIndex]

            val subject = schedulingRepository.getScheduleForSubject(randomSubject)

            _sessions.value = sessions.value + subject
        }
    }
}