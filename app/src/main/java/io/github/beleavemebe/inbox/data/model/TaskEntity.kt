package io.github.beleavemebe.inbox.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.beleavemebe.inbox.core.model.Task
import java.util.*

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "timestamp")
    val timestamp: Date = Date(),

    @ColumnInfo(name = "title")
    var title: String = "",

    @ColumnInfo(name = "is_completed")
    var isCompleted: Boolean = false,

    @ColumnInfo(name = "note")
    var note: String? = null,

    @ColumnInfo(name = "date")
    var dueDate: Date? = null,

    @ColumnInfo(name = "is_time_specified")
    var isTimeSpecified: Boolean? = false,
)

fun TaskEntity.toTask(): Task {
    return Task(
        id = id,
        title = title,
        note = note,
        isCompleted = isCompleted,
        timestamp = timestamp,
        dueDate = dueDate,
        isTimeSpecified = isTimeSpecified ?: false
    )
}

fun Task.toTaskEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        timestamp = timestamp,
        title = title,
        isCompleted = isCompleted,
        note = note,
        dueDate = dueDate,
        isTimeSpecified = isTimeSpecified
    )
}
