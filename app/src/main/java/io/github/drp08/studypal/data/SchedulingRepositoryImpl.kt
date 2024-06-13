package io.github.drp08.studypal.data

import android.util.Log
import io.github.drp08.studypal.db.daos.SessionDao
import io.github.drp08.studypal.db.daos.SubjectDao
import io.github.drp08.studypal.db.daos.TopicDao
import io.github.drp08.studypal.domain.SchedulingRepository
import io.github.drp08.studypal.domain.UserRepository
import io.github.drp08.studypal.domain.entities.SessionEntity
import io.github.drp08.studypal.domain.entities.SubjectEntity
import io.github.drp08.studypal.domain.entities.TopicEntity
import io.github.drp08.studypal.domain.models.PostBody
import io.github.drp08.studypal.domain.models.Session
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class SchedulingRepositoryImpl @Inject constructor(
    private val client: HttpClient,
    private val subjectDao: SubjectDao,
    private val topicDao: TopicDao,
    private val sessionDao: SessionDao,
    private val userRepository: UserRepository
) : SchedulingRepository {

    companion object {
        private const val TAG = "SchedulingRepositoryImp"
    }

    override suspend fun rescheduleAllSessions() = flow {
        val subjects = subjectDao.getAllSubjects()
        val topics = topicDao.getAllTopics()
        val sessions = sessionDao.getAllSessions()
        val user = userRepository.getUserLocal().first()
        val response = client.post("/schedule") {
            val body1 = Json.encodeToString(
                PostBody(
                    subjects.map(SubjectEntity::toSerializable).toTypedArray(),
                    sessions.map(SessionEntity::toSerializable).toTypedArray(),
                    topics.map(TopicEntity::toSerializable).toTypedArray(),
                    user
                )
            )
            setBody(body1)
        }
        if (response.status.isSuccess()) {
            val body = response.bodyAsText()
            Json.decodeFromString<List<Session>>(body)
                .forEach { sessionResponse ->
                    sessionDao.upsertSession(
                        SessionEntity.fromSerializable(
                            sessionResponse
                        )
                    )
                }
            emit(true)
        } else {
            Log.e(
                TAG,
                "rescheduleAllSessions: Response status is not successful. Body: ${response.bodyAsText()}",
                null
            )
            emit(false)
        }
    }
}