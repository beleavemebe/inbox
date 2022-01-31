package io.github.beleavemebe.inbox.core.usecase

import io.github.beleavemebe.inbox.core.model.Task
import io.github.beleavemebe.inbox.core.repository.TaskRepository

class GetTasks(private val taskRepository: TaskRepository) {
    suspend operator fun invoke() =
        taskRepository.getTasks()
}
