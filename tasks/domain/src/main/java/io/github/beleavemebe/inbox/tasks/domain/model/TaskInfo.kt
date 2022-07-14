package io.github.beleavemebe.inbox.tasks.domain.model

import java.util.*

data class TaskInfo(
    val id: Long = 0L,
    val created: Date = Date(),
    val taskId: UUID,
)
