package io.github.beleavemebe.inbox.core.internal

import io.github.beleavemebe.inbox.core.model.Task
import java.util.Comparator

object TaskSortingAlgorithm {
    fun sort(list: List<Task>): List<Task> {
        val comparator: Comparator<Task> =
            compareBy<Task> {
                it.isCompleted
            }.thenBy {
                it.dueDate == null
            }.thenBy {
                it.dueDate
            }.thenBy {
                it.timestamp
            }

        return list.sortedWith(comparator)
    }
}
