package io.github.beleavemebe.inbox.tasks.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.beleavemebe.inbox.tasks.domain.model.ChecklistItem
import java.util.*

@Entity(tableName = "task_checklist")
data class TaskChecklistEntity(
    @PrimaryKey
    val id: Long = 0,
    @ColumnInfo(name = "task_id")
    val taskId: UUID,
    @ColumnInfo(name = "content")
    val content: List<ChecklistItem> = emptyList(),
)
