package io.github.beleavemebe.inbox.domain.model

import java.util.*

data class TaskChecklist(
    val id: Long,
    val taskId: UUID,
    val content: List<ChecklistItem>,
) {
    operator fun plus(item: ChecklistItem) =
        copy(content = content + item)
}