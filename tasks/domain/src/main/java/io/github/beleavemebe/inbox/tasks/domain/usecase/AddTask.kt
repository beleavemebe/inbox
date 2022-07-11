package io.github.beleavemebe.inbox.tasks.domain.usecase

import io.github.beleavemebe.inbox.tasks.domain.model.Task
import io.github.beleavemebe.inbox.tasks.domain.repository.ChecklistRepository
import io.github.beleavemebe.inbox.tasks.domain.repository.TaskRepository

class AddTask(
    private val taskRepository: TaskRepository,
    private val checklistRepository: ChecklistRepository,
) {
    suspend operator fun invoke(task: Task) {
        if (task.checklist != null) {
            checklistRepository.insertChecklist(task.checklist)
        }

        taskRepository.insertTask(task)
    }
}
