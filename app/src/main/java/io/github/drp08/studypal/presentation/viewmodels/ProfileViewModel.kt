package io.github.drp08.studypal.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.drp08.studypal.db.daos.SubjectDao
import io.github.drp08.studypal.db.daos.TopicDao
import io.github.drp08.studypal.db.session.UserSession
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

    val subEnt = SubjectEntity("Calculus", 0.4f, 5,1,4,12, 23)
    val subEnt2 = SubjectEntity("LinearAlgebra", 0.6f, 12,4,8,9, 22)
    private val subEnt3 = SubjectEntity("Programming", 0.2f, 12,8,4,18, 21)
    private val sub = MutableStateFlow(listOf(subEnt3, subEnt2, subEnt))


    private val _subjects = MutableStateFlow<List<SubjectEntity>>(emptyList())
    //val subjects: StateFlow<List<SubjectEntity>> = _subjects.asStateFlow()
    val subjects: StateFlow<List<SubjectEntity>> = sub.asStateFlow()



    init {
        viewModelScope.launch {
            userSession.getUser().collectLatest {
                Log.d(TAG, "userEntity: $it")
            }
        }
    }

    private val _totalStudyHours = MutableStateFlow(0)
    val totalStudyHours: StateFlow<Int> = _totalStudyHours



    private fun loadUserSubjects() {
        viewModelScope.launch {
            val userSubjects = subjectDao.getAllSubjects()
            _subjects.value = userSubjects
        }
    }



    private fun calculateTotalStudyHours() {
        viewModelScope.launch {
            val studyHours = 9
            _totalStudyHours.value = studyHours
        }
    }


}

