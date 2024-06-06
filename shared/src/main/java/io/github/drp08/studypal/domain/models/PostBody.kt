package io.github.drp08.studypal.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class PostBody (
    val subjects: List<Subject>,
    val sessions: List<Session>,
    val topics: List<Topic>,
    val user: User
)