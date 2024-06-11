package io.github.drp08.studypal.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.drp08.studypal.db.daos.SubjectDao
import io.github.drp08.studypal.db.daos.TopicDao
import io.github.drp08.studypal.db.session.UserSession
import io.github.drp08.studypal.domain.entities.SessionEntity
import io.github.drp08.studypal.domain.entities.SubjectEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userSession: UserSession,
    private val topicDao: TopicDao,
    private val subjectDao: SubjectDao
): ViewModel() {
    companion object {
        private const val TAG = "ProfileViewModel"
    }

    private val _subjects = MutableStateFlow<List<SubjectEntity>>(emptyList())
    val subjects: StateFlow<List<SubjectEntity>> = _subjects.asStateFlow()

    init {
        viewModelScope.launch {
            userSession.getCurrentUser().collectLatest {
                Log.d(TAG, "userEntity: $it")
            }
        }
    }

//    fun getAllSubjects() : List<SubjectEntity> {
//        return subjectDao.getAllSubjects()
//    }
}