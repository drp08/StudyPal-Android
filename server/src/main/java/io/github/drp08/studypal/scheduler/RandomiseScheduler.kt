package io.github.drp08.studypal.scheduler

import io.github.drp08.studypal.domain.models.Session
import io.github.drp08.studypal.domain.models.Subject
import io.github.drp08.studypal.domain.models.Topic
import io.github.drp08.studypal.domain.models.User
import kotlin.random.Random

class RandomiseScheduler : Scheduler {
    override fun schedule(
        subjects: List<Subject>,
        topics: List<Topic>,
        fixedSessions: List<Session>,
        user: User
    ): List<Session> {
        val sessions: MutableList<Session> = mutableListOf<Session>().apply {
            addAll(fixedSessions)
        }

        // to ensure that a time isn't 'double-booked'
        val scheduledHours: MutableList<Int> = mutableListOf(0)

        for (studyHours in 0..user.maxStudyHours) {
            // chooses a random subject
            val subject: Subject = subjects.random()

            val subjectTopics: List<Topic> = topics.filter { it.subject == subject.name }
            if (subjectTopics.isEmpty()) // Make sure the subject have topics.
                continue

            // choose a random topic
            val topic: Topic = subjectTopics.random()

            // chooses a random start time that hasn't already been scheduled for
            var time = 0
            while (time in scheduledHours) {
                time = Random.nextInt(user.startWorkingHours, user.endWorkingHours)
            }
            scheduledHours.add(time)

            // create a new session
            val session =
                Session(
                    sessionId = Random.nextInt(1, Int.MAX_VALUE),
                    topic = topic.name,
                    startTime = time,
                    endTime = time + 60,
                )

            sessions.add(session)
        }
        return sessions.sortedBy { it.startTime }
    }
}