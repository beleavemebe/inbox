package io.github.beleavemebe.inbox.core.usecase

import io.github.beleavemebe.inbox.core.model.Task
import io.github.beleavemebe.inbox.core.repository.ChecklistRepository
import io.github.beleavemebe.inbox.core.repository.TaskRepository

class UpdateTask(
    private val taskRepository: TaskRepository,
    private val checklistRepository: ChecklistRepository,
) {
    suspend operator fun invoke(task: Task) {
        if (task.checklist != null) checklistRepository.insertChecklist(task.checklist)
        taskRepository.updateTask(task)
    }
}
