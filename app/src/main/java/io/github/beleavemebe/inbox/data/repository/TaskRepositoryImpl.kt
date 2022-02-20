package io.github.beleavemebe.inbox.data.repository

import io.github.beleavemebe.inbox.core.model.Task
import io.github.beleavemebe.inbox.core.repository.TaskRepository
import io.github.beleavemebe.inbox.data.db.TaskDao
import io.github.beleavemebe.inbox.data.db.TaskDatabase
import io.github.beleavemebe.inbox.data.model.TaskEntity
import io.github.beleavemebe.inbox.data.model.toTask
import io.github.beleavemebe.inbox.data.model.toTaskEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    database: TaskDatabase,
) : TaskRepository {
    private val taskDao: TaskDao = database.taskDao()

    override fun getTasks(): Flow<List<Task>> {
        return taskDao
            .getTasks()
            .map { dtoList ->
                dtoList.map(TaskEntity::toTask)
            }
    }

    override suspend fun getTaskById(id: UUID): Task {
        return taskDao.getTask(id).toTask()
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
}
