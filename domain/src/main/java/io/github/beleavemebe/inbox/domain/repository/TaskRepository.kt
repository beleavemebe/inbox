package io.github.beleavemebe.inbox.domain.repository

import io.github.beleavemebe.inbox.domain.model.Task
import kotlinx.coroutines.flow.Flow
import java.util.*

interface TaskRepository {
    fun getTasks(): Flow<List<Task>>

    suspend fun getTaskById(id: UUID): Task

    suspend fun insertTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(taskToDelete: Task)
}
