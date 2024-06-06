package io.github.drp08.studypal.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.drp08.studypal.db.daos.SessionDao
import io.github.drp08.studypal.db.daos.SubjectDao
import io.github.drp08.studypal.domain.SchedulingRepository
import io.github.drp08.studypal.presentation.models.HomeSessionItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(
    private val schedulingRepository: SchedulingRepository,
    private val subjectDao: SubjectDao,
    private val sessionDao: SessionDao
): ViewModel() {
    private val _sessions = MutableStateFlow<List<HomeSessionItem>>(emptyList())
    val items = _sessions.asStateFlow()

    init {
        viewModelScope.launch {
            subjectDao.getAllSubjects().collectLatest { subjectTopics ->
                for ((subject, topics) in subjectTopics) {
                    topics.forEach { topic ->
                        sessionDao.getSessionsOfTopic(topic.name).collectLatest { sessions ->
                            sessions.forEach { session ->
                                val newSession = HomeSessionItem(subject, topic, session)
                                val newSessionList = this@HomeViewModel.items.value + newSession
                                _sessions.value = newSessionList.sortedBy { it.session.startTime }
                            }
                        }
                    }
                }
            }
        }
    }
}