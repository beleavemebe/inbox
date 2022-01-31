package io.github.beleavemebe.inbox.core.usecase

import io.github.beleavemebe.inbox.core.model.Task
import io.github.beleavemebe.inbox.core.repository.TaskRepository

class UpdateTask(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(task: Task) =
        taskRepository.updateTask(task)
}
