package io.github.beleavemebe.inbox.data.model.mapper

import io.github.beleavemebe.inbox.domain.model.TaskChecklist
import io.github.beleavemebe.inbox.data.model.TaskChecklistEntity


fun TaskChecklist.toChecklistEntity(): TaskChecklistEntity {
    return TaskChecklistEntity(
        id = id,
        taskId = taskId,
        content = content
    )
}

fun TaskChecklistEntity.toChecklist(): TaskChecklist {
    return TaskChecklist(
        id = id,
        taskId = taskId,
        content = content
    )
}
