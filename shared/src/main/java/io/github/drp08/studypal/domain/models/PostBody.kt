package io.github.drp08.studypal.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class PostBody (
    val subjects: Array<Subject>,
    val sessions: Array<Session>,
    val topics: Array<Topic>,
    val user: User
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PostBody

        if (!subjects.contentEquals(other.subjects)) return false
        if (!sessions.contentEquals(other.sessions)) return false
        if (!topics.contentEquals(other.topics)) return false
        if (user != other.user) return false

        return true
    }

    override fun hashCode(): Int {
        var result = subjects.contentHashCode()
        result = 31 * result + sessions.contentHashCode()
        result = 31 * result + topics.contentHashCode()
        result = 31 * result + user.hashCode()
        return result
    }
}