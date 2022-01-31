package io.github.beleavemebe.inbox.core.repository

import io.github.beleavemebe.inbox.core.model.Task
import java.util.*

interface TaskRepository {
    suspend fun getTaskById(id: UUID): Task

    suspend fun getTasks(): List<Task>

    suspend fun addTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(taskToDelete: Task)
}
