package io.github.beleavemebe.inbox.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.model.Task

class TaskAdapter : ListAdapter<Task, TaskViewHolder>(TASK_DIFF_CALLBACK) {
    companion object {
        private const val TASK_DEFAULT = 1

        private val TASK_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val taskViewLayoutResource = when (viewType) {
            // place new layout variations here
            else -> R.layout.list_item_task
        }
        val taskView = LayoutInflater.from(parent.context)
            .inflate(taskViewLayoutResource, parent, false)
        return TaskViewHolder(taskView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }

    override fun getItemViewType(position: Int): Int {
        val task = getItem(position)
        return when (task) {
            // place conditions for new layout variations here
            else -> TASK_DEFAULT
        }
    }
}
