package io.github.beleavemebe.inbox.data.db

import androidx.room.*
import io.github.beleavemebe.inbox.data.model.TaskChecklistEntity
import io.github.beleavemebe.inbox.data.model.TaskEntity
import io.github.beleavemebe.inbox.data.model.TaskInfoEntity

@Database(
    entities = [TaskEntity::class, TaskChecklistEntity::class, TaskInfoEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun checklistDao(): ChecklistDao

    companion object {
        const val DATABASE_NAME = "task.db"
    }
}
