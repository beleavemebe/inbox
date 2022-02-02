package io.github.beleavemebe.inbox.core.model

import java.util.*

data class Task(
    val id: UUID = UUID.randomUUID(),
    var title: String = "",
    var note: String? = null,
    var isCompleted: Boolean = false,
    val timestamp: Date = Date(),
    var dueDate: Date? = null,
    var isTimeSpecified: Boolean? = null,
)
