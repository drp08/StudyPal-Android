package io.github.drp08.studypal.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.drp08.studypal.db.daos.SubjectDao
import io.github.drp08.studypal.db.daos.TopicDao
import io.github.drp08.studypal.db.session.UserSession
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


    init {
        viewModelScope.launch {
            userSession.getCurrentUser().collectLatest {
                Log.d(TAG, "userEntity: $it")
            }
        }
    }
}