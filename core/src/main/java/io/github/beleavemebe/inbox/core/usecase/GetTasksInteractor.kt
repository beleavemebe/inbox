package io.github.beleavemebe.inbox.core.usecase

import io.github.beleavemebe.inbox.core.internal.TaskFilterAlgorithm
import io.github.beleavemebe.inbox.core.internal.TaskSortingAlgorithm
import io.github.beleavemebe.inbox.core.model.Task
import io.github.beleavemebe.inbox.core.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTasksInteractor(private val taskRepository: TaskRepository) {
    private val _tasks: Flow<List<Task>>
        get() = taskRepository.getTasks().map {
            TaskSortingAlgorithm.sort(it)
        }

    fun getTasks() = _tasks

    fun getUndatedTasks() = _tasks.map { tasks ->
        TaskFilterAlgorithm.Undated.applyFilter(tasks)
    }

    fun getTasksDueThisWeek() = _tasks.map { tasks ->
        TaskFilterAlgorithm.DueThisWeek.applyFilter(tasks)
    }

    fun getTasksDueThisOrNextWeek() = _tasks.map { tasks ->
        TaskFilterAlgorithm.DueThisOrNextWeek.applyFilter(tasks)
    }
}
