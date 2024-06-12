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

    @Query("SELECT * FROM topic")
    suspend fun getAllTopics(): List<TopicEntity>

    @Query("SELECT * FROM topic JOIN session ON topic.name = session.parent")
    suspend fun getAllTopicsWithSessions(): Map<TopicEntity, List<SessionEntity>>
}