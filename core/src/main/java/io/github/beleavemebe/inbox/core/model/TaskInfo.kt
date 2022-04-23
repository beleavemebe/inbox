package io.github.beleavemebe.inbox.core.model

import java.util.*

data class TaskInfo(
    val id: Long = 0L,
    val created: Date = Date(),
    val taskId: UUID,
)
