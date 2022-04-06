package io.github.beleavemebe.inbox.data.model

import androidx.room.Embedded
import androidx.room.Relation
import io.github.beleavemebe.inbox.core.model.Task
import io.github.beleavemebe.inbox.data.model.mapper.toChecklist
import io.github.beleavemebe.inbox.data.model.mapper.toTaskInfo

data class TaskDetails(
    @Embedded
    val taskEntity: TaskEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "task_id")
    val checklistEntity: TaskChecklistEntity?,

    @Relation(
        parentColumn = "id",
        entityColumn = "task_id")
    val infoEntity: TaskInfoEntity?,
)
