package io.github.beleavemebe.inbox.core.usecase

import io.github.beleavemebe.inbox.core.repository.TaskRepository

class GetTasks(private val taskRepository: TaskRepository) {
    operator fun invoke() =
        taskRepository.getTasks()
}
