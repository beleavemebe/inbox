package io.github.beleavemebe.inbox.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import io.github.beleavemebe.inbox.db.task.TaskDatabase
import io.github.beleavemebe.inbox.model.Task
import java.util.*

class TaskRepository private constructor(context: Context) {
    companion object {
        private const val DATABASE_NAME = "task-db"
        private var INSTANCE: TaskRepository? = null

        fun getInstance() = INSTANCE
            ?: throw IllegalStateException("TaskRepository was not initialized")

        fun initialize(context: Context) {
            if (INSTANCE == null) INSTANCE = TaskRepository(context)
        }
    }

    private val fifthMigration = object : Migration(5, 6) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.apply {
                execSQL("CREATE TABLE `Task_New` (`id` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `title` TEXT NOT NULL, `is_completed` INTEGER NOT NULL, `note` TEXT, `date` INTEGER, `is_time_specified` INTEGER, PRIMARY KEY(`id`))")
                execSQL("INSERT INTO `Task_New` (`id`, `timestamp`, `title`, `is_completed`, `note`, `date`, `is_time_specified`) SELECT id, `timestamp`, `title`, `is_completed`, `note`, `date`, `is_time_specified` FROM `Task`")
                execSQL("DROP TABLE `Task`")
                execSQL("ALTER TABLE `Task_New` RENAME TO `Task`")
            }
        }
    }

    private val database: TaskDatabase = Room.databaseBuilder(
        context.applicationContext,
        TaskDatabase::class.java,
        DATABASE_NAME
    ).addMigrations(fifthMigration).build()

    private val taskDao = database.taskDao()

    fun getTask(id: UUID): LiveData<Task> = taskDao.getTask(id)
    fun getTasks(): LiveData<List<Task>> = taskDao.getTasks()

    suspend fun addTask(task: Task) = taskDao.addTask(task)

    suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    suspend fun deleteTask(taskToDelete: Task) = taskDao.deleteTask(taskToDelete)
}
