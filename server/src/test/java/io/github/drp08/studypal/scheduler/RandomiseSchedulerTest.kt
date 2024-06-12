package io.github.drp08.studypal.scheduler

import io.github.drp08.studypal.domain.models.Subject
import io.github.drp08.studypal.domain.models.Topic
import io.github.drp08.studypal.domain.models.User
import io.github.drp08.studypal.scheduler.Scheduler.Companion.HOUR_IN_MILLIS
import io.github.drp08.studypal.utils.formatTime
import io.github.drp08.studypal.utils.toEpochMilliSecond
import java.time.LocalDate
import java.time.ZoneId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class RandomiseSchedulerTest {

    companion object {
        private val user by lazy {
            User(
                name = "Nishant",
                startWorkingHours = 8 * HOUR_IN_MILLIS,
                endWorkingHours = 20 * HOUR_IN_MILLIS,
                maxStudyingHours = 5
            )
        }

        private val startOfDay =
            LocalDate.now().atStartOfDay().atZone(ZoneId.of("UTC")).toEpochMilliSecond()

        private val userStartTime = user.startWorkingHours + startOfDay
        private val userEndTime = user.endWorkingHours + startOfDay

        private val formatPattern = "dd/MM/yyyy HH:mm:ss"
    }

    private object FakeSubjects {
        val maths by lazy {
            Subject(
                name = "Maths",
                confidenceLevel = .1f,
                totalSessions = 0,
                completedSessions = 0,
                scheduledSessions = 0,
                hoursPerWeek = 10,
                examEpoch = null
            )
        }

        val cs by lazy {
            Subject(
                name = "CS",
                confidenceLevel = .1f,
                totalSessions = 0,
                completedSessions = 0,
                scheduledSessions = 0,
                hoursPerWeek = 8,
                examEpoch = null
            )
        }

        val english by lazy {
            Subject(
                name = "English",
                confidenceLevel = .1f,
                totalSessions = 0,
                completedSessions = 0,
                scheduledSessions = 0,
                hoursPerWeek = 12,
                examEpoch = null
            )
        }
    }

    private object FakeTopics {
        val algebra by lazy { Topic("Algebra", "Maths") }
        val calculus by lazy { Topic("Calculus", "Maths") }
        val statistics by lazy { Topic("Statistics", "Maths") }

        val kotlin by lazy { Topic("Kotlin", "CS") }
        val c by lazy { Topic("C", "CS") }
        val haskell by lazy { Topic("Haskell", "CS") }

        val literature by lazy { Topic("Literature", "English") }
        val language by lazy { Topic("Language", "English") }
    }

    private val scheduler: Scheduler = RandomiseScheduler()

    @Test
    fun `returns nothing if empty subjects and sessions are passed`() {
        val result = scheduler.schedule(emptyList(), emptyList(), emptyList(), user)

        assertEquals(0, result.size)
    }

    @Test
    fun `returns nothing if empty sessions are passed`() {
        val result = scheduler.schedule(listOf(FakeSubjects.maths), emptyList(), emptyList(), user)

        assertEquals(0, result.size)
    }

    @Test
    fun `returns sessions scheduled for 1 subject and 1 topic`() {
        val result = scheduler.schedule(
            listOf(FakeSubjects.maths),
            listOf(FakeTopics.algebra),
            emptyList(),
            user
        )

        assertNotEquals(0, result.size)
        println("INFO: ${result.size} sessions scheduled")
        for (session in result) {
            assertEquals(1 * HOUR_IN_MILLIS, session.endTime - session.startTime)
            assert(session.startTime >= userStartTime) {
                "User's startTime: ${formatTime(userStartTime, formatPattern)}\n" +
                        "Session was started at: ${formatTime(session.startTime, formatPattern)}."
            }
            assert(session.endTime <= userEndTime) {
                "User's endTime: ${formatTime(userEndTime, formatPattern)}\n" +
                        "Session ended at: ${formatTime(session.endTime, formatPattern)}."
            }
        }
    }

    @Test
    fun `returns sessions scheduled with many subjects and topics`() {
        val subjects = listOf(FakeSubjects.maths, FakeSubjects.cs, FakeSubjects.english)
        val topics = listOf(
            FakeTopics.algebra,
            FakeTopics.calculus,
            FakeTopics.statistics,
            FakeTopics.kotlin,
            FakeTopics.c,
            FakeTopics.haskell,
            FakeTopics.literature,
            FakeTopics.language
        )

        val result = scheduler.schedule(
            subjects,
            topics,
            emptyList(),
            user
        )

        val scheduledTimes = mutableListOf<LongRange>()

        assertNotEquals(0, result.size)
        println("INFO: ${result.size} sessions scheduled")
        for (session in result) {
            assertEquals(1 * HOUR_IN_MILLIS, session.endTime - session.startTime)
            assert(session.startTime >= userStartTime) {
                "User's startTime: ${formatTime(userStartTime, formatPattern)}\n" +
                        "Session was started at: ${formatTime(session.startTime, formatPattern)}."
            }
            assert(session.endTime <= userEndTime) {
                "User's endTime: ${formatTime(userEndTime, formatPattern)}\n" +
                        "Session ended at: ${formatTime(session.endTime, formatPattern)}."
            }

            scheduledTimes.forEach { range ->
                assert(session.startTime !in range) {
                    "Session ${session.topic} was scheduled for ${
                        formatTime(
                            session.startTime,
                            formatPattern
                        )
                    } " +
                            "in the middle of another session scheduled between " +
                            "${formatTime(range.first, formatPattern)} and ${
                                formatTime(
                                    range.last,
                                    formatPattern
                                )
                            }"
                }
                assert(session.endTime !in range) {
                    "Session ${session.topic} was scheduled for ${
                        formatTime(
                            session.startTime,
                            formatPattern
                        )
                    } " +
                            "in the middle of another session scheduled between " +
                            "${formatTime(range.first, formatPattern)} and ${
                                formatTime(
                                    range.last,
                                    formatPattern
                                )
                            }"
                }
            }

            scheduledTimes.add(session.startTime..session.endTime)
        }

        val subjectToScheduledTime = result.groupBy { it.topic }.map { (topic, sessions) ->
            topics.find { it.name == topic }!!.let(Topic::subject) to sessions
        }.associate { (subject, sessions) ->
            subjects.find { it.name == subject }!! to sessions
        }.mapValues { (_, sessions) ->
            sessions.sumOf { it.endTime - it.startTime }
        }

        for ((subject, scheduledTime) in subjectToScheduledTime) {
            assert(scheduledTime <= subject.hoursPerWeek * HOUR_IN_MILLIS) {
                "Subject: ${subject.name} was scheduled for " +
                        "${
                            formatTime(
                                scheduledTime,
                                "HH:mm:ss"
                            )
                        } hours, more than ${subject.hoursPerWeek} hours."
            }
        }

        val totalScheduledTime = subjectToScheduledTime.values.sum()
        assert(totalScheduledTime <= user.maxStudyingHours * HOUR_IN_MILLIS) {
            "Total scheduled Time: ${formatTime(totalScheduledTime, "HH:mm:ss")} hours, " +
                    "more than ${user.maxStudyingHours} hours."
        }
    }
}

