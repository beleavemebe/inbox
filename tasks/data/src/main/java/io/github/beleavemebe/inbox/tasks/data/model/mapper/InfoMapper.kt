package io.github.beleavemebe.inbox.tasks.data.model.mapper

import io.github.beleavemebe.inbox.tasks.domain.model.TaskInfo
import io.github.beleavemebe.inbox.tasks.data.model.TaskInfoEntity

fun TaskInfo.toInfoEntity(): TaskInfoEntity {
    return TaskInfoEntity(
        id = id,
        created = created,
        taskId = taskId,
    )
}

fun TaskInfoEntity.toTaskInfo(): TaskInfo {
    return TaskInfo(
        id = id,
        created = created,
        taskId = taskId,
    )
}
