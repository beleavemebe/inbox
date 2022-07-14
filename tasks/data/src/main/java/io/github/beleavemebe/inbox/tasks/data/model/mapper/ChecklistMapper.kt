package io.github.beleavemebe.inbox.tasks.data.model.mapper

import io.github.beleavemebe.inbox.tasks.domain.model.TaskChecklist
import io.github.beleavemebe.inbox.tasks.data.model.TaskChecklistEntity


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
