package io.github.drp08.studypal.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.drp08.studypal.domain.models.Topic

@Entity(tableName = "topic")
data class TopicEntity (
    @PrimaryKey
    val name: String,
    val subject: String
){
    fun toSerializable(): Topic {
        return Topic(name, subject)
    }

    companion object {
        fun fromSerializable(topic: Topic) : TopicEntity{
            return TopicEntity(
                topic.name,
                topic.subject
            )
        }
    }
}