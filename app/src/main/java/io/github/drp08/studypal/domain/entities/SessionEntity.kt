package io.github.drp08.studypal.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.drp08.studypal.domain.models.Session
import io.github.drp08.studypal.domain.models.Subject
import io.github.drp08.studypal.domain.models.Topic

@Entity
data class SessionEntity(
    @PrimaryKey
    val sessionId:Int,
    val subject: Subject,
    val topic: Topic,
    val startTime: Int, // Second of the day
    val endTime: Int, // Second of the day
    val totalSessions: Int
) {
    fun toSerializable(): Session {
        return Session(
            sessionId,
            subject,
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
                session.subject,
                session.topic,
                session.startTime,
                session.endTime,
                session.totalSessions
            )
        }
    }
}