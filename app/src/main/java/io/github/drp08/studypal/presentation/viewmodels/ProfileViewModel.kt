package io.github.drp08.studypal.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.drp08.studypal.db.daos.SubjectDao
import io.github.drp08.studypal.domain.FriendRepository
import io.github.drp08.studypal.domain.entities.SubjectEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val subjectDao: SubjectDao
): ViewModel() {
    private val auth by lazy { Firebase.auth }
    private val db by lazy { Firebase.firestore }

    companion object {
        private const val TAG = "ProfileViewModel"

        private val calculus = SubjectEntity("Calculus", 0.4f, 5,1,4,12, 23)
        private val linearAlgebra = SubjectEntity("LinearAlgebra", 0.6f, 12,4,8,9, 22)
        private val programming = SubjectEntity("Programming", 0.2f, 12,8,4,18, 21)
        val dummySubjects = listOf(programming, linearAlgebra, calculus)
    }

    private val _subjects = MutableStateFlow<List<SubjectEntity>>(emptyList())
    val subjects: StateFlow<List<SubjectEntity>> = _subjects.asStateFlow()

    var realXp by mutableIntStateOf(0)
        private set

    init {
        viewModelScope.launch {
            db.collection("users")
                .document(auth.currentUser!!.uid)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error)
                        return@addSnapshotListener
                    }

                    if (value != null && value.exists()) {
                        realXp = value.getLong("xp")!!.toInt()
                    }
                }
        }
    }

    init {
        _subjects.value = dummySubjects
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

