package io.github.drp08.studypal.domain.scheduler

import io.github.drp08.studypal.domain.entities.SessionEntity
import io.github.drp08.studypal.domain.entities.SubjectEntity
import io.github.drp08.studypal.domain.entities.TopicEntity
import io.github.drp08.studypal.domain.models.Session
import io.github.drp08.studypal.domain.models.User
import io.github.drp08.studypal.domain.scheduler.Scheduler.Companion.HOUR_IN_MILLIS
import io.github.drp08.studypal.utils.toEpochMilliSecond
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import kotlin.random.Random

class RandomiseScheduler @Inject constructor() : Scheduler {
    companion object {
        private const val SESSION_DURATION = 1 * HOUR_IN_MILLIS
    }

    override fun schedule(
        subjects: List<SubjectEntity>,
        topics: List<TopicEntity>,
        fixedSessions: List<SessionEntity>,
        user: User
    ): List<SessionEntity> {
        val sessions = mutableListOf<SessionEntity>()

        val startOfDay =
            LocalDate.now().atStartOfDay().atZone(ZoneId.of("UTC")).toEpochMilliSecond()

        // to ensure that a time isn't 'double-booked'
        val scheduledHours = mutableListOf<LongRange>()
        val subjectTotalTime = mutableMapOf<String, Long>()

        var trials = 0
        var studyHoursOfDay = 0L
        outer@ while (studyHoursOfDay < user.maxStudyingHours * HOUR_IN_MILLIS) {
            // chooses a random subject
            val subject = subjects
                .filter { (subjectTotalTime[it.name] ?: 0) < it.hoursPerWeek * HOUR_IN_MILLIS }
                .filter { subject -> topics.any { it.subject == subject.name } }
                .also { if (it.isEmpty()) return emptyList() }
                .random()

            val subjectTopics = topics.filter { it.subject == subject.name }
            if (subjectTopics.isEmpty()) // Make sure the subject have topics.
                continue

            // choose a random topic
            val topic = subjectTopics.random()

            // epoch time from the start of the day to the start of the working hour
            val startTime = startOfDay + user.startWorkingHours
            val endTime = startOfDay + user.endWorkingHours

            // chooses a random start time that hasn't already been scheduled for
            var time: Long
            do {
                time = Random.nextLong(startTime, endTime - SESSION_DURATION)
                if (++trials > 100)
                    continue@outer
            } while (scheduledHours.any { time in it || (time + SESSION_DURATION) in it })

            scheduledHours.add(time..(time + SESSION_DURATION))

            // create a new session
            val session =
                SessionEntity(
                    topic = topic.name,
                    startTime = time,
                    endTime = time + SESSION_DURATION,
                )

            sessions.add(session)
            subjectTotalTime[subject.name] =
                (subjectTotalTime[subject.name] ?: 0) + SESSION_DURATION
            studyHoursOfDay += SESSION_DURATION
        }
        return sessions
            .apply { removeAll(fixedSessions) }
            .sortedBy { it.startTime }
    }
}