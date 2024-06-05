package io.github.drp08.studypal.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.drp08.studypal.domain.models.Topic

@Entity
data class TopicEntity (
    @PrimaryKey
    val name: String
){
    fun toSerializable(): Topic {
        return Topic(name)
    }
    companion object {
        fun fromSerializable(topic: Topic) : TopicEntity{
            return TopicEntity(
                topic.name
            )
        }
    }
}