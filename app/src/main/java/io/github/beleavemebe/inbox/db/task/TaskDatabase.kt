package io.github.beleavemebe.inbox.db.task

import androidx.room.*
import io.github.beleavemebe.inbox.model.Task

@Database(
    entities = [Task::class],
    version = 6
)
@TypeConverters(TaskTypeConverters::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
