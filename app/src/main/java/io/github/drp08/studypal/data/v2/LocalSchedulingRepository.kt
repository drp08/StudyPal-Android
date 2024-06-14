package io.github.drp08.studypal.data.v2

import io.github.drp08.studypal.db.daos.SessionDao
import io.github.drp08.studypal.db.daos.SubjectDao
import io.github.drp08.studypal.db.daos.TopicDao
import io.github.drp08.studypal.domain.SchedulingRepository
import io.github.drp08.studypal.domain.UserRepository
import io.github.drp08.studypal.domain.scheduler.Scheduler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalSchedulingRepository @Inject constructor(
    private val subjectDao: SubjectDao,
    private val topicDao: TopicDao,
    private val sessionDao: SessionDao,
    private val userRepository: UserRepository,
    private val scheduler: Scheduler
): SchedulingRepository {
    override suspend fun rescheduleAllSessions() = flow {
        val subjects = subjectDao.getAllSubjects()
        val topics = topicDao.getAllTopics()
        val user = userRepository.getUser()

        val newSessions = scheduler.schedule(subjects, topics, emptyList(), user)
        newSessions.forEach {
            sessionDao.upsertSession(it)
        }
        emit(true)
    }
}