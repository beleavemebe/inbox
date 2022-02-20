package io.github.beleavemebe.inbox.data.db

import androidx.room.*
import io.github.beleavemebe.inbox.data.model.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 8,
    autoMigrations = [AutoMigration(from = 7, to = 8)]
)
@TypeConverters(TaskTypeConverters::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
