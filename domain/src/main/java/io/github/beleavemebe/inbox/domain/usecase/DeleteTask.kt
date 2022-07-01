package io.github.beleavemebe.inbox.domain.usecase

import io.github.beleavemebe.inbox.domain.model.Task
import io.github.beleavemebe.inbox.domain.repository.TaskRepository

class DeleteTask(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(task: Task) =
        taskRepository.deleteTask(task)
}
