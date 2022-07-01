package io.github.beleavemebe.inbox.domain.usecase

import io.github.beleavemebe.inbox.domain.model.Task
import io.github.beleavemebe.inbox.domain.repository.ChecklistRepository
import io.github.beleavemebe.inbox.domain.repository.TaskRepository

class UpdateTask(
    private val taskRepository: TaskRepository,
    private val checklistRepository: ChecklistRepository,
) {
    suspend operator fun invoke(task: Task) {
        if (task.checklist != null) checklistRepository.insertChecklist(task.checklist)
        taskRepository.updateTask(task)
    }
}
