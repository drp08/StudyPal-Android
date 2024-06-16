package io.github.drp08.studypal.presentation.models

import kotlinx.datetime.LocalTime

data class DailyViewEvent(
    val startTime: LocalTime,
    val endTime: LocalTime,
    val title: String
)