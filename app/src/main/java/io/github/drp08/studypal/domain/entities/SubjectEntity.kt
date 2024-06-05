package io.github.drp08.studypal.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.drp08.studypal.domain.models.Subject

@Entity(tableName = "subject")
data class SubjectEntity(
    @PrimaryKey
    val name: String,
    val confidenceLevel: Int,
    val totalSessions: Int,
    val completedSessions: Int,
    val scheduledSessions: Int,
) {
    fun toSerializable() : Subject {
        return Subject(
            name,
            confidenceLevel,
            totalSessions,
            completedSessions,
            scheduledSessions
        )
    }
    companion object {
        fun fromSerializable(subject: Subject) : SubjectEntity{
            return SubjectEntity(
                subject.name,
                subject.confidenceLevel,
                subject.totalSessions,
                subject.completedSessions,
                subject.scheduledSessions
            )
        }
    }
}