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
import io.github.drp08.studypal.domain.models.PostBody
import io.github.drp08.studypal.domain.models.Session
import io.github.drp08.studypal.routes.Schedule
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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

    override suspend fun rescheduleAllSessions() = channelFlow {
        val subjects = subjectDao.getAllSubjects()
        val topics = topicDao.getAllTopics()
        val sessions = sessionDao.getAllSessions()
        val users = userDao.getUser()

        try {
            subjects.collectLatest { subs ->
                topics.collectLatest { tops ->
                    sessions.collectLatest { sess ->
                        users.collectLatest { user ->
                            val response = client.post(Schedule()) {
                                val body1 = Json.encodeToString(
                                    PostBody(
                                        subs.map(SubjectEntity::toSerializable).toTypedArray(),
                                        sess.map(SessionEntity::toSerializable).toTypedArray(),
                                        tops.map(TopicEntity::toSerializable).toTypedArray(),
                                        user.toSerializable()
                                    )
                                )
                                Log.d(TAG, "rescheduleAllSessions: $body1")
                                setBody(
                                    body1

                                )
                            }
                            if (response.status.isSuccess()) {
                                send(true)
                                val body = response.bodyAsText()
                                Log.d(TAG, "rescheduleAllSessions: $body")
                                Json.decodeFromString<List<Session>>(body)
                                    .also {
                                        Log.d(TAG, "rescheduleAllSessions: $it")
                                    }
                                    .forEach { sessionResponse ->
                                        sessionDao.upsertSession(
                                            SessionEntity.fromSerializable(
                                                sessionResponse
                                            )
                                        )
                                    }
                            } else {
                                Log.e(TAG, "rescheduleAllSessions: ${response.bodyAsText()}", null)
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "rescheduleAllSessions: ${e.message}", e)
        } finally {
            send(false)
        }
    }
}