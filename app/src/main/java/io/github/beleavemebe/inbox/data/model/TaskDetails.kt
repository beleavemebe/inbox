package io.github.beleavemebe.inbox.data.model

import androidx.room.Embedded
import androidx.room.Relation

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
