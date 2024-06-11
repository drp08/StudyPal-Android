package io.github.drp08.studypal.utils

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone

/**
 * @see DateTimeFormatter.ofPattern
 */
fun formatTime(
    epochMillis: Long,
    pattern: String
): String {
    val instant = Instant.ofEpochMilli(epochMillis)
    val localDateTime = instant.atZone(ZoneId.of("UTC")).toLocalDateTime()

    val formatter = DateTimeFormatter.ofPattern(pattern)
    return localDateTime.format(formatter)
}

fun ZonedDateTime.toEpochMilliSecond(): Long = toEpochSecond() * 1000L
