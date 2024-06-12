package io.github.drp08.studypal.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.drp08.studypal.domain.models.Session


@Entity(tableName = "session")
data class SessionEntity(
    @PrimaryKey
    val sessionId: Int,
    val parent: String, // The foreign to the topic name
    val startTime: Long, // Epoch millis
    val endTime: Long, // Epoch millis
) {
    fun toSerializable(): Session {
        return Session(
            sessionId,
            parent,
            startTime,
            endTime
        )
    }

    companion object {
        fun fromSerializable(session: Session): SessionEntity {
            return SessionEntity(
                session.sessionId,
                session.parent,
                session.startTime,
                session.endTime
            )
        }
    }
}