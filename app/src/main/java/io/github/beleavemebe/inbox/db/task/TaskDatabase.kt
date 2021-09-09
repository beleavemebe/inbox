package io.github.beleavemebe.inbox.db.task

import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import io.github.beleavemebe.inbox.model.Task

@Database(
    entities = [Task::class],
    version = 2,
    autoMigrations = [
        AutoMigration(
            from = 1,
            to = 2,
            spec = TaskDatabase.FirstMigration::class
        )
    ]
)
@TypeConverters(TaskTypeConverters::class)
abstract class TaskDatabase : RoomDatabase(), AutoMigrationSpec {
    abstract fun taskDao(): TaskDao

    @RenameColumn(
        tableName = "Task",
        fromColumnName = "repetitionReferenceTimestamp",
        toColumnName = "rep_ref_timestamp"
    )
    class FirstMigration : AutoMigrationSpec
}