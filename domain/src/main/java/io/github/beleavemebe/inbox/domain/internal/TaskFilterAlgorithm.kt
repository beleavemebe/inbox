package io.github.beleavemebe.inbox.domain.internal

import io.github.beleavemebe.inbox.domain.common.util.*
import io.github.beleavemebe.inbox.domain.model.Task

internal sealed interface TaskFilterAlgorithm {

    fun applyFilter(source: List<Task>): List<Task>

    object DueThisWeek : TaskFilterAlgorithm {
        override fun applyFilter(source: List<Task>): List<Task> {
            return source
                .filter { it.dueDate != null }
                .filter {
                    val dueDateMs = it.dueDate!!.time
                    dueDateMs in lastMonday..weekEnd
                }
        }
    }

    object DueThisOrNextWeek : TaskFilterAlgorithm {
        override fun applyFilter(source: List<Task>): List<Task> {
            return source
                .filter { it.dueDate != null }
                .filter {
                    val dueDateMs = it.dueDate!!.time
                    dueDateMs in lastMonday..nextWeekEnd
                }
        }
    }

    object Undated : TaskFilterAlgorithm {
        override fun applyFilter(source: List<Task>): List<Task> {
            return source
                .filter { it.dueDate == null }
        }
    }
}