package io.github.drp08.studypal.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.drp08.studypal.domain.models.Session


@Entity(tableName = "session")
data class SessionEntity(
    @PrimaryKey
    val sessionId: Int,
    val topic: String, // The foreign to the topic name
    val startTime: Int, // Second of the day
    val endTime: Int, // Second of the day
) {
    fun toSerializable(): Session {
        return Session(
            sessionId,
            topic,
            startTime,
            endTime
        )
    }

    companion object {
        fun fromSerializable(session: Session): SessionEntity {
            return SessionEntity(
                session.sessionId,
                session.topic,
                session.startTime,
                session.endTime
            )
        }
    }
}