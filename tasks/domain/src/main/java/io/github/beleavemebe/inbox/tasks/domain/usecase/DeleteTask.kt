package io.github.beleavemebe.inbox.tasks.domain.usecase

import io.github.beleavemebe.inbox.tasks.domain.model.Task
import io.github.beleavemebe.inbox.tasks.domain.repository.TaskRepository

class DeleteTask(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(task: Task) =
        taskRepository.deleteTask(task)
}
