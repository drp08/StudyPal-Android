package io.github.drp08.studypal.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Subject(
    val name: String,
    var confidenceLevel: Int,
    var totalNumOfSessions: Int,
    var numSessionsCompleted: Int,
    var numSessionsScheduled: Int,
//    var topics: List<Topic>
)