package io.github.drp08.studypal.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * @see DateTimeFormatter.ofPattern
 */
fun formatTime(
    epochMillis: Long,
    pattern: String
): String {
    val instant = Instant.ofEpochMilli(epochMillis)
    val localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime()

    val formatter = DateTimeFormatter.ofPattern(pattern)
    return localDateTime.format(formatter)
}
