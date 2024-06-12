package io.github.drp08.studypal.utils

import kotlin.test.Test
import kotlin.test.assertEquals

class UtilsTest {

    @Test
    fun `formatTime returns correct time format`() {
        val epochMillis = 1 * 60 * 60 * 1000L

        val formattedTime = formatTime(epochMillis, "HH:mm:ss")

        assertEquals("01:00:00", formattedTime)
    }
}