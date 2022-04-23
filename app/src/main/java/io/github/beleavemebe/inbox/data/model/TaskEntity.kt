package io.github.beleavemebe.inbox.data.model

import androidx.room.*
import io.github.beleavemebe.inbox.core.model.Task
import java.util.*

@Entity(
    tableName = "task",
    indices = [
        Index("checklist_id"),
        Index("info_id")
    ],
    foreignKeys = [
        ForeignKey(
            entity = TaskChecklistEntity::class,
            parentColumns = ["id"],
            childColumns = ["checklist_id"],
        ),
        ForeignKey(
            entity = TaskInfoEntity::class,
            parentColumns = ["id"],
            childColumns = ["info_id"],
        )
    ],
)
data class TaskEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "title")
    var title: String = "",

    @ColumnInfo(name = "is_completed")
    var isCompleted: Boolean = false,

    @ColumnInfo(name = "note")
    var note: String = "",

    @ColumnInfo(name = "date")
    var dueDate: Date? = null,

    @ColumnInfo(name = "is_time_specified")
    var isTimeSpecified: Boolean = false,

    @ColumnInfo(name = "checklist_id")
    var checklistId: Long? = null,

    @ColumnInfo(name = "info_id")
    var infoId: Long? = null,
)


fun Task.toTaskEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        isCompleted = isCompleted,
        note = note,
        dueDate = dueDate,
        isTimeSpecified = isTimeSpecified,
        checklistId = checklist?.id,
        infoId = info?.id
    )
}
