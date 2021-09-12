package io.github.beleavemebe.inbox.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import io.github.beleavemebe.inbox.db.task.TaskDatabase
import io.github.beleavemebe.inbox.model.Task
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

class TaskRepository private constructor(context: Context) {
    companion object {
        private const val DATABASE_NAME = "task-db"
        private var INSTANCE : TaskRepository? = null

        fun getInstance() = INSTANCE
            ?: throw IllegalStateException("Repo was not initialized")

        fun initialize(context: Context) {
            if (INSTANCE == null) INSTANCE = TaskRepository(context)
        }
    }

    private val database: TaskDatabase = Room.databaseBuilder(
        context.applicationContext,
        TaskDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val taskDao = database.taskDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getTask(id : UUID) : LiveData<Task?>      = taskDao.getTask(id)
    fun getTasks()         : LiveData<List<Task>> = taskDao.getTasks()

    fun updateTask(task: Task)         = executor.execute { taskDao.updateTask(task) }
    fun addTask(task: Task)            = executor.execute { taskDao.addTask(task) }
    fun deleteTask(taskToDelete: Task) = executor.execute { taskDao.deleteTask(taskToDelete) }
    fun deleteTask(taskId: UUID)       = executor.execute { taskDao.deleteTask(taskId) }
    fun deleteCompletedTasks()         = executor.execute { taskDao.deleteCompletedTasks() }
}