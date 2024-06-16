package io.github.drp08.studypal.db.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import io.github.drp08.studypal.domain.entities.SessionEntity
import io.github.drp08.studypal.domain.entities.SubjectEntity
import io.github.drp08.studypal.domain.entities.TopicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {

    @Upsert
    suspend fun upsertSession(sessionEntity: SessionEntity)

    @Query("SELECT * from session")
    suspend fun getAllSessions(): List<SessionEntity>

    @Query("SELECT * from session WHERE session.topic = :topic")
    suspend fun getSessionsOfTopic(topic: String): List<SessionEntity>

    @Query("SELECT * from session WHERE session.topic = :topic")
    fun getRealtimeSessionsOfTopic(topic: String): Flow<List<SessionEntity>>

    @Query("SELECT * FROM session JOIN topic ON session.topic = topic.name JOIN subject ON topic.subject = subject.name")
    fun getSessionsWithSubjectAndTopic(): Flow<Map<SubjectEntity, Map<TopicEntity, List<SessionEntity>>>>
}