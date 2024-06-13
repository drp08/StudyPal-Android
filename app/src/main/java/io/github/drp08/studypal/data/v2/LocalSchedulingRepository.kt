package io.github.drp08.studypal.data.v2

import io.github.drp08.studypal.domain.SchedulingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalSchedulingRepository @Inject constructor(): SchedulingRepository {
    override suspend fun rescheduleAllSessions(): Flow<Boolean> {
        TODO("Not yet implemented")
    }
}