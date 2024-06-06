package io.github.drp08.studypal.presentation.models

import io.github.drp08.studypal.domain.entities.SessionEntity
import io.github.drp08.studypal.domain.entities.SubjectEntity
import io.github.drp08.studypal.domain.entities.TopicEntity

data class HomeSessionItem(
    val subject: SubjectEntity,
    val topic: TopicEntity,
    val session: SessionEntity
)
