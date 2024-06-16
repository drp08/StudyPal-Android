package io.github.drp08.studypal.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.drp08.studypal.db.daos.SessionDao
import io.github.drp08.studypal.db.daos.SubjectDao
import io.github.drp08.studypal.presentation.models.HomeSessionItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val subjectDao: SubjectDao,
    private val sessionDao: SessionDao
) : ViewModel() {
    private val _sessions = MutableStateFlow<List<HomeSessionItem>>(emptyList())
    val items = _sessions.asStateFlow()

    companion object {
        private const val TAG = "HomeViewModel"
    }

    init {
        viewModelScope.launch {
//            subjectDao.getRealtimeSubjectsWithTopics().collect { subjectTopics ->
//                for ((subject, topics) in subjectTopics) {
//                    topics.forEach { topic ->
//                        sessionDao.getRealtimeSessionsOfTopic(topic.name)
//                            .collect { sessions ->
//                                sessions.forEach { session ->
//                                    val newSession = HomeSessionItem(subject, topic, session)
//                                    val newSessionList =
//                                        (this@HomeViewModel.items.value + newSession).distinct()
//                                    _sessions.value =
//                                        newSessionList.sortedBy { it.session.startTime }
//                                }
//                            }
//                    }
//                }
//            }

            sessionDao.getSessionsWithSubjectAndTopic().collectLatest {
                Log.d(TAG, "getSessionsWithSubjectAndTopic: $it")
                for ((subject, topicSessions) in it) {
                    for ((topic, sessions) in topicSessions) {
                        sessions.forEach { session ->
                            val newSession = HomeSessionItem(subject, topic, session)
                            val newSessionList = (this@HomeViewModel.items.value + newSession).distinct()
                            _sessions.value = newSessionList.sortedBy { it.session.startTime }
                        }

                    }
                }
            }
        }
    }
}