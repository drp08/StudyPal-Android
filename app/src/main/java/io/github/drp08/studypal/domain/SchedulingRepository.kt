package io.github.drp08.studypal.domain

import kotlinx.coroutines.flow.Flow

interface SchedulingRepository {
    suspend fun rescheduleAllSessions(): Flow<Boolean>
}