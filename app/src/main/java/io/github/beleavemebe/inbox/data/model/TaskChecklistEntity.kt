package io.github.beleavemebe.inbox.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import io.github.beleavemebe.inbox.core.model.ChecklistItem
import io.github.beleavemebe.inbox.core.model.TaskChecklist
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
