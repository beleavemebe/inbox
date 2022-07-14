package io.github.beleavemebe.inbox.tasks.domain.model

import java.util.*

data class Task(
    val id: UUID = UUID.randomUUID(),
    val title: String = "",
    val note: String = "",
    val isCompleted: Boolean = false,
    val dueDate: Date? = null,
    val isTimeSpecified: Boolean = false,
    val checklist: TaskChecklist? = null,
    val info: TaskInfo? = null,
)
