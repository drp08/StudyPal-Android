package io.github.drp08.studypal.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.drp08.studypal.domain.models.Session


@Entity(tableName = "session")
data class SessionEntity(
    val topic: String, // The foreign to the topic name
    val startTime: Long, // Epoch millis
    val endTime: Long, // Epoch millis
    @PrimaryKey(autoGenerate = true)
    val sessionId: Int = 0,
) {
    fun toSerializable(): Session {
        return Session(
            topic,
            startTime,
            endTime
        )
    }

    companion object {
        fun fromSerializable(session: Session): SessionEntity {
            return SessionEntity(
                session.topic,
                session.startTime,
                session.endTime
            )
        }
    }
}