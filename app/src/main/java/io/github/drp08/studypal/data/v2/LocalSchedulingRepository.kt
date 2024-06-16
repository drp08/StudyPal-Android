package io.github.drp08.studypal.data.v2

import android.util.Log
import io.github.drp08.studypal.db.daos.SessionDao
import io.github.drp08.studypal.db.daos.SubjectDao
import io.github.drp08.studypal.db.daos.TopicDao
import io.github.drp08.studypal.domain.SchedulingRepository
import io.github.drp08.studypal.domain.UserRepository
import io.github.drp08.studypal.domain.scheduler.Scheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalSchedulingRepository @Inject constructor(
    private val subjectDao: SubjectDao,
    private val topicDao: TopicDao,
    private val sessionDao: SessionDao,
    private val userRepository: UserRepository,
    private val scheduler: Scheduler
): SchedulingRepository {

    companion object {
        private const val TAG = "LocalSchedulingReposito"
    }

    override suspend fun rescheduleAllSessions() = flow {
        Log.d(TAG, "rescheduleAllSessions() called")
        val subjects = subjectDao.getAllSubjects()
        val topics = topicDao.getAllTopics()
        val user = userRepository.getUser()

        withContext(Dispatchers.IO) {
            val newSessions = scheduler.schedule(subjects, topics, emptyList(), user)
            Log.d(TAG, "rescheduleAllSessions: returned newSessions = $newSessions")
            newSessions.forEach {
                Log.d(TAG, "rescheduleAllSessions: inserting $it into sessionDao")
                sessionDao.upsertSession(it)
            }
        }
        emit(true)
    }
}