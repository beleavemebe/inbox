package io.github.beleavemebe.inbox.tasks.domain.usecase

import io.github.beleavemebe.inbox.tasks.domain.repository.TaskRepository
import java.util.*

class GetTaskById(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(id: UUID) =
        taskRepository.getTaskById(id)
}
