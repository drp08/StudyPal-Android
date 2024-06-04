package io.github.drp08.studypal.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.drp08.studypal.domain.models.Session

@Entity
data class SessionEntity(
    @PrimaryKey
    val name: String,
    val startTime: Int, // Second of the day
    val endTime: Int, // Second of the day
    val totalSessions: Int
) {
    fun toSerializable(): Session {
        return Session(
            name,
            startTime,
            endTime,
            totalSessions
        )
    }

    companion object {
        fun fromSerializable(session: Session): SessionEntity {
            return SessionEntity(
                session.name,
                session.startTime,
                session.endTime,
                session.totalSessions
            )
        }
    }
}