package io.github.drp08.studypal.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Session(
    val topic: String,
    val startTime: Long, // Epoch millis
    val endTime: Long, // Epoch millis
)