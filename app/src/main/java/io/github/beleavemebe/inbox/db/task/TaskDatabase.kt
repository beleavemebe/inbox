package io.github.beleavemebe.inbox.db.task

import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import io.github.beleavemebe.inbox.model.Task

@Database(
    entities = [Task::class],
    version = 5,
    autoMigrations = [
        AutoMigration(from = 4, to = 5)
    ]
)
@TypeConverters(TaskTypeConverters::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    @DeleteColumn(
        tableName = "Task",
        columnName = "duration"
    )
    class ThirdMigration : AutoMigrationSpec

}