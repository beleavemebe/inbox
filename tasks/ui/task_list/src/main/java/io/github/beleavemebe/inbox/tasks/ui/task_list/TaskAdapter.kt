package io.github.beleavemebe.inbox.tasks.ui.task_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import io.github.beleavemebe.inbox.core.utils.refillWith
import io.github.beleavemebe.inbox.tasks.domain.model.Task
import io.github.beleavemebe.inbox.tasks.ui.task_list.databinding.ListItemTaskBinding
import java.util.*

class TaskAdapter(
    private val callback: ListUpdateCallback,
    private val onTaskClicked: (UUID) -> Unit,
    private val onTaskChecked: (UUID, Boolean) -> Unit,
) : RecyclerView.Adapter<TaskViewHolder>() {

    private val tasks = mutableListOf<Task>()

    fun setContent(content: List<Task>) {
        val diffCallback = TaskDiffCallback(tasks, content)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        tasks.refillWith(content)
        diffResult.dispatchUpdatesTo(this)
        diffResult.dispatchUpdatesTo(callback)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ListItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding, onTaskClicked, onTaskChecked)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
    }
}
