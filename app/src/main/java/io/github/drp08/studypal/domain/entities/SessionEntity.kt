package io.github.drp08.studypal.domain.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import io.github.drp08.studypal.domain.models.Session
import io.github.drp08.studypal.domain.models.Subject
import io.github.drp08.studypal.domain.models.Topic

@Entity(tableName = "session")
data class SessionEntity(
    @PrimaryKey
    val sessionId: Int,
    val topic: String, // The foreign to the topic name
    val startTime: Long, // Second of the day
    val endTime: Long, // Second of the day
    val totalSessions: Int
) {
    fun toSerializable(): Session {
        return Session(
            sessionId,
            topic,
            startTime,
            endTime,
            totalSessions
        )
    }

    companion object {
        fun fromSerializable(session: Session): SessionEntity {
            return SessionEntity(
                session.sessionId,
                session.topic,
                session.startTime,
                session.endTime,
                session.totalSessions
            )
        }
    }
}