package io.github.beleavemebe.inbox.tasks.data.di

import io.github.beleavemebe.inbox.tasks.domain.repository.ChecklistRepository
import io.github.beleavemebe.inbox.tasks.domain.repository.TaskRepository

interface TasksDataApi {
    val taskRepository: TaskRepository
    val checklistRepository: ChecklistRepository

    companion object {
        fun create(deps: TasksDataDependencies): TasksDataApi {
            return DaggerTasksDataComponent.factory().create(deps)
        }
    }
}
