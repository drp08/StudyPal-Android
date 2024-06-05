package io.github.drp08.studypal.presentation

import io.github.drp08.studypal.db.daos.SessionDao
import io.github.drp08.studypal.db.daos.SubjectDao
import io.github.drp08.studypal.domain.entities.SessionEntity
import io.github.drp08.studypal.domain.entities.TopicEntity
import io.github.drp08.studypal.domain.models.Session
import io.github.drp08.studypal.domain.models.Subject
import io.github.drp08.studypal.domain.models.Topic
import io.github.drp08.studypal.domain.models.User
import kotlinx.coroutines.flow.first
import kotlin.random.Random

// Entities are stored on the DB
// Subject is sent to the Server which returns a List<Session>
// SubjectEntity is used to store on the Db
class Scheduler {
    suspend fun randomiseSessions(subjectDao: SubjectDao, sessionsDao: SessionDao, user: User) : List<Session> {
        var sessions: MutableList<Session> = mutableListOf()

        var maxHours = user.maxNumberOfStudyHours
        var scheduledHours: MutableList<Int> = mutableListOf(0) // to ensure that a time isn't 'double-booked'
        var studyHours = 0 // how long the user has studies for
        var subjectsToTopics = subjectDao.getAllSubjects()

        while (studyHours != maxHours){
            val subjectMap = subjectsToTopics.first()

            // chooses a random subject
            val subject: Subject = subjectMap.keys.random().toSerializable()
            var scheduledNoHours = subject.scheduledSessions

            val topics: List<TopicEntity> = subjectMap.getOrDefault(subject,listOf())!! // TODO fix this
            if (topics.isNotEmpty()){ // subject exists
                // choose a random topic
                val topic: Topic = topics.random().toSerializable()

                // chooses a random start time that hasn't already been scheduled for
                var time = 0
                while (scheduledHours.contains(time)){
                    time = Random.nextInt(user.startWorkingHours,user.endWorkingHours)
                }
                scheduledHours.add(time)

                // create a new session
                val session =
                    Session(
                    sessionId = Random.nextInt(67289),
                    topic = topic.name,
                    startTime = time,
                    endTime = time + 60,
                    totalSessions = 5
                )

                // adding the session to the DB
                sessionsDao.upsertSession(SessionEntity.fromSerializable(session))

                scheduledNoHours += 1
                studyHours += 1
            }
        }
        return sessions.sortedBy { it.startTime }
    }
}