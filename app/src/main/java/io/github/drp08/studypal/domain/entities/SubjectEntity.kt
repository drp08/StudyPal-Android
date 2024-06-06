package io.github.drp08.studypal.domain.entities

import androidx.annotation.FloatRange
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.drp08.studypal.domain.models.Subject

@Entity(tableName = "subject")
data class SubjectEntity(
    @PrimaryKey
    val name: String,
    @FloatRange(from = 0.0, to = 1.0)
    val confidenceLevel: Float,
    val totalSessions: Int,
    val completedSessions: Int,
    val scheduledSessions: Int,
    val hoursPerWeek: Int,
    val examEpoch: Long
) {
    fun toSerializable() : Subject {
        return Subject(
            name,
            confidenceLevel,
            totalSessions,
            completedSessions,
            scheduledSessions,
            hoursPerWeek,
            examEpoch
        )
    }
    companion object {
        fun fromSerializable(subject: Subject) : SubjectEntity{
            return SubjectEntity(
                subject.name,
                subject.confidenceLevel,
                subject.totalSessions,
                subject.completedSessions,
                subject.scheduledSessions,
                subject.hoursPerWeek,
                subject.examEpoch
            )
        }
    }
}