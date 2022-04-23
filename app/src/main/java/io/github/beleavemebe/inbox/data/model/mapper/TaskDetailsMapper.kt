package io.github.beleavemebe.inbox.data.model.mapper

import io.github.beleavemebe.inbox.core.model.Task
import io.github.beleavemebe.inbox.data.model.TaskDetails
import io.github.beleavemebe.inbox.data.model.toTaskEntity

fun TaskDetails.toTask(): Task {
    return Task(
        id = taskEntity.id,
        title = taskEntity.title,
        note = taskEntity.note,
        isCompleted = taskEntity.isCompleted,
        dueDate = taskEntity.dueDate,
        isTimeSpecified = taskEntity.isTimeSpecified,
        checklist = checklistEntity?.toChecklist(),
        info = infoEntity?.toTaskInfo()
    )
}
