package io.github.beleavemebe.inbox.data.model.mapper

import io.github.beleavemebe.inbox.domain.model.Task
import io.github.beleavemebe.inbox.data.model.TaskDetails

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
