package io.github.drp08.studypal.db.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import io.github.drp08.studypal.domain.entities.SessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {

    @Upsert
    suspend fun upsertSession(sessionEntity: SessionEntity)

    @Query("SELECT * from session")
    fun getAllSessions(): Flow<List<SessionEntity>>

    @Query("SELECT * from session WHERE session.topic = :topic")
    fun getSessionsOfTopic(topic: String): Flow<List<SessionEntity>>
}