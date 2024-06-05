package io.github.drp08.studypal.db.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import io.github.drp08.studypal.domain.entities.TopicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicDao {
    @Upsert
    suspend fun upsertTopic(topicEntity: TopicEntity)

    @Query("SELECT * from topicentity")
    fun getAllTopics(): Flow<List<TopicEntity>>
}