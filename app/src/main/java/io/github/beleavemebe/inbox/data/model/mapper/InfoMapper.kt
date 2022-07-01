package io.github.beleavemebe.inbox.data.model.mapper

import io.github.beleavemebe.inbox.domain.model.TaskInfo
import io.github.beleavemebe.inbox.data.model.TaskInfoEntity

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
