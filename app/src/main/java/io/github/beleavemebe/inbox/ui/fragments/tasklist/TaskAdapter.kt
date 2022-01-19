package io.github.beleavemebe.inbox.ui.fragments.tasklist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.github.beleavemebe.inbox.databinding.ListItemTaskBinding
import io.github.beleavemebe.inbox.model.Task
import java.util.*

class TaskAdapter(
    private val onTaskClicked: (UUID) -> Unit,
) : ListAdapter<Task, TaskViewHolder>(TASK_DIFF_CALLBACK) {
    companion object {
        private val TASK_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Task>()
        {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ListItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding, onTaskClicked)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }

    override fun onViewRecycled(holder: TaskViewHolder) {
        super.onViewRecycled(holder)
        holder.invalidateTask()
    }
}
