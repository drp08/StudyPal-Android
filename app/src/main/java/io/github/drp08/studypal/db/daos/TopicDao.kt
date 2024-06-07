package io.github.drp08.studypal.db.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import io.github.drp08.studypal.domain.entities.SessionEntity
import io.github.drp08.studypal.domain.entities.TopicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicDao {

    @Upsert
    suspend fun upsertTopic(topic: TopicEntity)

    @Query("SELECT * FROM topic JOIN session ON topic.name = session.topic")
    fun getAllTopicsWithSessions(): Flow<Map<TopicEntity, List<SessionEntity>>>

    @Query("SELECT * FROM topic")
    fun getAllTopics(): Flow<List<TopicEntity>>

}