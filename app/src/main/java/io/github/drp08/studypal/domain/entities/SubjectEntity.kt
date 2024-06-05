package io.github.drp08.studypal.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.drp08.studypal.domain.models.Subject
import io.github.drp08.studypal.domain.models.Topic

@Entity
data class SubjectEntity(
    @PrimaryKey
    val name: String,
    var confidenceLevel: Int,
    var totalNumOfSessions: Int,
    var numSessionsCompleted: Int,
    var numSessionsScheduled: Int,
//    var topics: List<Topic>
) {
    fun toSerializable() : Subject {
        return Subject(
            name,
            confidenceLevel,
            totalNumOfSessions,
            numSessionsCompleted,
            numSessionsScheduled,
//            topics
        )
    }
    companion object {
        fun fromSerializable(subject: Subject) : SubjectEntity{
            return SubjectEntity(
                subject.name,
                subject.confidenceLevel,
                subject.totalNumOfSessions,
                subject.numSessionsCompleted,
                subject.numSessionsScheduled,
//                subject.topics
            )
        }
    }
}