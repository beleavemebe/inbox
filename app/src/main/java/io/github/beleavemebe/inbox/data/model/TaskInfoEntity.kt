package io.github.beleavemebe.inbox.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.beleavemebe.inbox.core.model.TaskInfo
import java.util.*

@Entity(tableName = "task_info")
data class TaskInfoEntity(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "created")
    val created: Date = Date(),
    @ColumnInfo(name = "task_id")
    val taskId: UUID,
)
