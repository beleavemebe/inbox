package io.github.beleavemebe.inbox.core.repository

import io.github.beleavemebe.inbox.core.model.Task
import kotlinx.coroutines.flow.Flow
import java.util.*

interface TaskRepository {
    fun getTasks(): Flow<List<Task>>

    suspend fun getTaskById(id: UUID): Task

    suspend fun addTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(taskToDelete: Task)
}
