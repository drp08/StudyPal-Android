package io.github.drp08.studypal.scheduler

import io.github.drp08.studypal.domain.models.Session
import io.github.drp08.studypal.domain.models.Subject
import io.github.drp08.studypal.domain.models.Topic
import io.github.drp08.studypal.domain.models.User
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import kotlin.random.Random

class RandomiseScheduler : Scheduler {
    override fun schedule(
        subjects: List<Subject>,
        topics: List<Topic>,
        fixedSessions: List<Session>,
        user: User
    ): List<Session> {
        val millisecondsInHour = 60*60*10*10*10 // 3600 seconds * 10^3
        val sessions: MutableList<Session> = mutableListOf<Session>().apply {
            addAll(fixedSessions)
        }

        val startOfDay = LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond()

        // to ensure that a time isn't 'double-booked'
        val scheduledHours: MutableList<Long> = mutableListOf(startOfDay)

        for (studyHours in 0..user.maxStudyingHours) {
            // chooses a random subject
            val subject: Subject = subjects.random()

            val subjectTopics: List<Topic> = topics.filter { it.subject == subject.name }
            if (subjectTopics.isEmpty()) // Make sure the subject have topics.
                continue

            // choose a random topic
            val topic: Topic = subjectTopics.random()

            // chooses a random start time that hasn't already been scheduled for
            val startTime: Long = startOfDay + user.startWorkingHours // epoch time from the start of the day to the start of the working hour
            val endTime: Long = startOfDay + user.endWorkingHours

            var time: Long = startOfDay // epoch time

            while (time in scheduledHours) {
                // generate a random epoch time between
                time = Random.nextLong(startTime,endTime)
//                time = Random.nextInt(user.startWorkingHours.toInt(), user.endWorkingHours.toInt()).toLong()
            }
            scheduledHours.add(time)

            // create a new session
            val session =
                Session(
                    sessionId = Random.nextInt(1, Int.MAX_VALUE),
                    topic = topic.name,
                    startTime = time,
                    endTime = time + millisecondsInHour,
                )

            sessions.add(session)
        }
        return sessions.sortedBy { it.startTime }
    }
}