package io.github.beleavemebe.inbox.data.repository

import android.content.Context
import androidx.room.Room
import io.github.beleavemebe.inbox.core.model.Task
import io.github.beleavemebe.inbox.core.repository.TaskRepository
import io.github.beleavemebe.inbox.data.db.TaskDao
import io.github.beleavemebe.inbox.data.db.TaskDatabase
import io.github.beleavemebe.inbox.data.model.TaskEntity
import io.github.beleavemebe.inbox.data.model.toTask
import io.github.beleavemebe.inbox.data.model.toTaskEntity
import java.util.*

class TaskRepositoryImpl(context: Context) : TaskRepository {
    private val database = Room.databaseBuilder(
        context.applicationContext,
        TaskDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val taskDao: TaskDao = database.taskDao()

    override suspend fun getTaskById(id: UUID): Task {
        return taskDao.getTask(id).toTask()
    }

    override suspend fun getTasks(): List<Task> {
        return taskDao.getTasks().map(TaskEntity::toTask)
    }

    override suspend fun addTask(task: Task) {
        taskDao.addTask(task.toTaskEntity())
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toTaskEntity())
    }

    override suspend fun deleteTask(taskToDelete: Task) {
        taskDao.deleteTask(taskToDelete.toTaskEntity())
    }

    companion object {
        private const val DATABASE_NAME = "task-db"
    }
}
