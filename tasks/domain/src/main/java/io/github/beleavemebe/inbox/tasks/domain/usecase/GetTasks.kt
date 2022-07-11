package io.github.beleavemebe.inbox.tasks.domain.usecase

import io.github.beleavemebe.inbox.tasks.domain.internal.TaskFilteringAlgorithm
import io.github.beleavemebe.inbox.tasks.domain.internal.TaskSortingAlgorithm
import io.github.beleavemebe.inbox.tasks.domain.model.Task
import io.github.beleavemebe.inbox.tasks.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTasks(private val taskRepository: TaskRepository) {
    private val _tasks: Flow<List<Task>>
        get() = taskRepository.getTasks().map {
            TaskSortingAlgorithm.sort(it)
        }

    fun getTasks() = _tasks

    fun getUndatedTasks() = _tasks.map { tasks ->
        TaskFilteringAlgorithm.Undated.applyFilter(tasks)
    }

    fun getTasksDueThisWeek() = _tasks.map { tasks ->
        TaskFilteringAlgorithm.DueThisWeek.applyFilter(tasks)
    }

    fun getTasksDueThisOrNextWeek() = _tasks.map { tasks ->
        TaskFilteringAlgorithm.DueThisOrNextWeek.applyFilter(tasks)
    }
}
