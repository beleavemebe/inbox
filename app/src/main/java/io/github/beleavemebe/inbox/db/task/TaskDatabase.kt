package io.github.beleavemebe.inbox.db.task

import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import io.github.beleavemebe.inbox.model.Task

@Database(
    entities = [Task::class],
    version = 3
)
@TypeConverters(TaskTypeConverters::class)
abstract class TaskDatabase : RoomDatabase(), AutoMigrationSpec {
    abstract fun taskDao(): TaskDao
}