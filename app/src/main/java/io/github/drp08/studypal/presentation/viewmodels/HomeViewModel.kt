package io.github.drp08.studypal.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.drp08.studypal.db.daos.SessionDao
import io.github.drp08.studypal.db.daos.SubjectDao
import io.github.drp08.studypal.domain.UserRepository
import io.github.drp08.studypal.presentation.models.HomeSessionItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sessionDao: SessionDao,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _sessions = MutableStateFlow<List<HomeSessionItem>>(emptyList())
    val items = _sessions.asStateFlow()

    companion object {
        private const val TAG = "HomeViewModel"
    }

    init {
        viewModelScope.launch {
            sessionDao.getSessionsWithSubjectAndTopic().collectLatest { subjectTopicsSessions ->
                Log.d(TAG, "getSessionsWithSubjectAndTopic: $subjectTopicsSessions")
                for ((subject, topicSessions) in subjectTopicsSessions) {
                    for ((topic, sessions) in topicSessions) {
                        sessions.forEach { session ->
                            val newSession = HomeSessionItem(subject, topic, session)
                            val newSessionList = (this@HomeViewModel.items.value + newSession).distinct()
                            val currentTime = System.currentTimeMillis()
                            _sessions.value = newSessionList
                                .filter { it.session.startTime > currentTime }
                                .sortedBy { it.session.startTime }
                        }
                    }
                }
            }
        }
    }
    fun updateXP(){
        viewModelScope.launch{
            userRepository.updateXp()
        }
    }
}