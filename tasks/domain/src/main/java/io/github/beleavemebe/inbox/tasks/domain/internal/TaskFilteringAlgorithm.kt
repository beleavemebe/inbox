package io.github.beleavemebe.inbox.tasks.domain.internal

import io.github.beleavemebe.inbox.common.lastMonday
import io.github.beleavemebe.inbox.common.nextWeekEnd
import io.github.beleavemebe.inbox.common.weekEnd
import io.github.beleavemebe.inbox.tasks.domain.model.Task

internal sealed interface TaskFilteringAlgorithm {

    fun applyFilter(source: List<Task>): List<Task>

    object DueThisWeek : TaskFilteringAlgorithm {
        override fun applyFilter(source: List<Task>): List<Task> {
            return source
                .filter { it.dueDate != null }
                .filter {
                    val dueDateMs = it.dueDate!!.time
                    dueDateMs in lastMonday..weekEnd
                }
        }
    }

    object DueThisOrNextWeek : TaskFilteringAlgorithm {
        override fun applyFilter(source: List<Task>): List<Task> {
            return source
                .filter { it.dueDate != null }
                .filter {
                    val dueDateMs = it.dueDate!!.time
                    dueDateMs in lastMonday..nextWeekEnd
                }
        }
    }

    object Undated : TaskFilteringAlgorithm {
        override fun applyFilter(source: List<Task>): List<Task> {
            return source
                .filter { it.dueDate == null }
        }
    }
}
