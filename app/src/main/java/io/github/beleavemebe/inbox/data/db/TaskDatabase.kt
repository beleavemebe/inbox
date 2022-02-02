package io.github.beleavemebe.inbox.data.db

import androidx.room.*
import io.github.beleavemebe.inbox.data.model.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 7,
    autoMigrations = [AutoMigration(from = 6, to = 7)]
)
@TypeConverters(TaskTypeConverters::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
