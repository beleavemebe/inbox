package io.github.beleavemebe.inbox.core.internal

import io.github.beleavemebe.inbox.core.common.util.*
import io.github.beleavemebe.inbox.core.model.Task

internal sealed interface TaskFilterAlgorithm {

    fun applyFilter(source: List<Task>): List<Task>

    object DueThisWeek : TaskFilterAlgorithm {
        override fun applyFilter(source: List<Task>): List<Task> {
            return source
                .filter { it.dueDate != null }
                .filter {
                    val dueDateMs = it.dueDate!!.time
                    dueDateMs in todayMs..weekEndMs
                }
        }
    }

    object DueThisOrNextWeek : TaskFilterAlgorithm {
        override fun applyFilter(source: List<Task>): List<Task> {
            return source
                .filter { it.dueDate != null }
                .filter {
                    val dueDateMs = it.dueDate!!.time
                    dueDateMs in todayMs..nextWeekEndMs
                }
        }
    }
}
