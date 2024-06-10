package io.github.drp08.studypal.presentation.viewmodels

import androidx.lifecycle.ViewModel
import io.github.drp08.studypal.db.daos.SubjectDao
import io.github.drp08.studypal.db.daos.TopicDao
import io.github.drp08.studypal.db.daos.UserDao

class ProfileViewModel(
    private val subjectDao: SubjectDao,
    private val topicDao: TopicDao,
    private val userDao: UserDao
) : ViewModel() {

}