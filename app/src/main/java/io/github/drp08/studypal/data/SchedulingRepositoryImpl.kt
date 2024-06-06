package io.github.drp08.studypal.data

import android.util.Log
import io.github.drp08.studypal.db.daos.SessionDao
import io.github.drp08.studypal.db.daos.SubjectDao
import io.github.drp08.studypal.db.daos.TopicDao
import io.github.drp08.studypal.db.daos.UserDao
import io.github.drp08.studypal.domain.SchedulingRepository
import io.github.drp08.studypal.domain.entities.SessionEntity
import io.github.drp08.studypal.domain.entities.SubjectEntity
import io.github.drp08.studypal.domain.entities.TopicEntity
import io.github.drp08.studypal.domain.models.Session
import io.github.drp08.studypal.routes.Schedule
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

class SchedulingRepositoryImpl(
    private val client: HttpClient,
    private val subjectDao: SubjectDao,
    private val topicDao: TopicDao,
    private val sessionDao: SessionDao,
    private val userDao: UserDao
) : SchedulingRepository {

    companion object {
        private const val TAG = "SchedulingRepositoryImp"
    }

    override suspend fun rescheduleAllSessions() = flow {
        val subjects = subjectDao.getAllSubjects()
        val topics = topicDao.getAllTopics()
        val sessions = sessionDao.getAllSessions()
        val users = userDao.getUser()

        try {
            subjects.collectLatest { subs ->
                topics.collectLatest { tops ->
                    sessions.collectLatest { sess ->
                        users.collectLatest { user ->
                            val response = client.get(Schedule(
                                subjects = subs.map(SubjectEntity::toSerializable),
                                topics = tops.map(TopicEntity::toSerializable),
                                sessions = sess.map(SessionEntity::toSerializable),
                                user = user.toSerializable()
                            ))
                            emit(response.status.isSuccess())

                            response.body<List<Session>>().forEach { sessionResponse ->
                                sessionDao.upsertSession(SessionEntity.fromSerializable(sessionResponse))
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "rescheduleAllSessions: ${e.message}", e)
        } finally {
            emit(false)
        }
    }
}