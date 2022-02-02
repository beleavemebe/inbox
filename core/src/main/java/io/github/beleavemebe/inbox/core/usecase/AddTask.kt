package io.github.beleavemebe.inbox.core.usecase

import io.github.beleavemebe.inbox.core.model.Task
import io.github.beleavemebe.inbox.core.repository.TaskRepository

class AddTask(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(task: Task) =
        taskRepository.addTask(task)
}
