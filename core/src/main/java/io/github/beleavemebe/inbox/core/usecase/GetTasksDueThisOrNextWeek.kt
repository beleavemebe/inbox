package io.github.beleavemebe.inbox.core.usecase

import io.github.beleavemebe.inbox.core.internal.TaskFilterAlgorithm
import io.github.beleavemebe.inbox.core.repository.TaskRepository
import kotlinx.coroutines.flow.map

class GetTasksDueThisOrNextWeek(private val taskRepository: TaskRepository) {
    operator fun invoke() =
        taskRepository.getTasks().map { tasks ->
            TaskFilterAlgorithm.DueThisOrNextWeek.applyFilter(tasks)
        }
}
