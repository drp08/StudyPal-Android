package io.github.drp08.studypal.scheduler

import io.github.drp08.studypal.domain.models.Session
import io.github.drp08.studypal.domain.models.Subject
import io.github.drp08.studypal.domain.models.Topic
import io.github.drp08.studypal.domain.models.User
import io.github.drp08.studypal.scheduler.Scheduler.Companion.HOUR_IN_MILLIS
import io.github.drp08.studypal.utils.toEpochMilliSecond
import java.time.LocalDate
import java.time.ZoneId
import kotlin.random.Random

class RandomiseScheduler : Scheduler {
    companion object {
        private const val SESSION_DURATION = 1 * HOUR_IN_MILLIS
    }

    override fun schedule(
        subjects: List<Subject>,
        topics: List<Topic>,
        fixedSessions: List<Session>,
        user: User
    ): List<Session> {
        val sessions: MutableList<Session> = mutableListOf<Session>().apply {
            addAll(fixedSessions)
        }

        val startOfDay =
            LocalDate.now().atStartOfDay().atZone(ZoneId.of("UTC")).toEpochMilliSecond()

        // to ensure that a time isn't 'double-booked'
        val scheduledHours = mutableListOf<LongRange>()
        val subjectTotalTime = mutableMapOf<String, Long>()

        var trials = 0
        outer@ for (studyHours in 0..user.maxStudyingHours) {
            // chooses a random subject
            val subject: Subject = subjects
                .filter { subjectTotalTime.getOrDefault(it.name, 0) < it.hoursPerWeek * HOUR_IN_MILLIS }
                .also { if (it.isEmpty()) return emptyList() }
                .random()

            val subjectTopics: List<Topic> = topics.filter { it.subject == subject.name }
            if (subjectTopics.isEmpty()) // Make sure the subject have topics.
                continue

            // choose a random topic
            val topic: Topic = subjectTopics.random()

            // chooses a random start time that hasn't already been scheduled for
            val startTime: Long =
                startOfDay + user.startWorkingHours // epoch time from the start of the day to the start of the working hour
            val endTime: Long = startOfDay + user.endWorkingHours

            var time: Long
            do {
                time = Random.nextLong(startTime, endTime - SESSION_DURATION)
                if (++trials > 100)
                    continue@outer
            } while (scheduledHours.any { time in it || (time + SESSION_DURATION) in it })

            scheduledHours.add(time..(time + SESSION_DURATION))

            // create a new session
            val session =
                Session(
                    sessionId = Random.nextInt(1, Int.MAX_VALUE),
                    topic = topic.name,
                    startTime = time,
                    endTime = time + SESSION_DURATION,
                )

            sessions.add(session)
            subjectTotalTime[subject.name] =
                subjectTotalTime.getOrDefault(subject.name, 0) + SESSION_DURATION
        }
        return sessions
            .apply { removeAll(fixedSessions) }
            .sortedBy { it.startTime }
    }
}