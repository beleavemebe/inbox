package io.github.beleavemebe.inbox.ui.fragments.tasklist

import android.content.res.Resources
import android.text.format.DateFormat
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.databinding.ListItemTaskBinding
import io.github.beleavemebe.inbox.model.Task
import io.github.beleavemebe.inbox.repositories.TaskRepository
import io.github.beleavemebe.inbox.util.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TaskViewHolder(
    private val binding: ListItemTaskBinding
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener
{
    var task: Task? = null
        private set

    fun bind(task: Task) {
        this.task = task
        binding.initTitleTv(task)
        binding.initCompletedCb(task)
        binding.initDatetimeBar(task)
        binding.alterViewIfTaskIsCompleted(task)
    }

    private fun ListItemTaskBinding.initTitleTv(task: Task) {
        taskTitleTv.text = task.title
    }

    private fun ListItemTaskBinding.initCompletedCb(task: Task) {
        completedCb.apply {
            isChecked = task.isCompleted
            setOnCheckedChangeListener { _, isChecked ->
                setTaskCompleted(isChecked)
            }
        }
    }

    private fun ListItemTaskBinding.initDatetimeBar(task: Task) {
        val taskDate = task.date
        val resources = datetimeBar.resources
        datetimeBar.isVisible = taskDate != null
        if (taskDate != null) {
            taskTimeTv.text = getDatetimeText(task, resources)
            updateDatetimeBarColor(task, resources)
        }
    }

    private fun ListItemTaskBinding.alterViewIfTaskIsCompleted(task: Task) {
        taskTitleTv.apply {
            val titleTextColor = getTitleColor(task, resources)
            setCrossedOut(task.isCompleted)
            setTextColor(titleTextColor)
            updateDatetimeBarColor(task, resources)
        }
    }

    private fun ListItemTaskBinding.updateDatetimeBarColor(task: Task, res: Resources) {
        val datetimeBarColor = getDatetimeBarColor(task, res)
        taskTimeIv.setColorFilter(datetimeBarColor)
        taskTimeTv.apply {
            setTextColor(datetimeBarColor)
            setCrossedOut(task.isCompleted)
        }
    }

    private fun getDatetimeText(task: Task, resources : Resources): String {
        val dateText = when {
            task.date.isYesterday -> resources.getString(R.string.yesterday)
            task.date.isToday -> resources.getString(R.string.today)
            task.date.isTomorrow -> resources.getString(R.string.tomorrow)
            else -> DateFormat
                .format("EEE, dd MMM", task.date).toString()
                .replaceFirstChar { it.uppercase() }
        }

        val timeText = if (task.isTimeSpecified == true) {
            DateFormat.format("HH:mm", task.date).toString()
        } else ""

        return resources.getString(R.string.task_datetime_placeholder, dateText, timeText)
    }

    init {
        binding.root.setOnClickListener(this)
    }

    private fun setTaskCompleted(flag: Boolean) {
        task?.let {
            it.isCompleted = flag
            MainScope().launch { TaskRepository.getInstance().updateTask(it) }
            binding.alterViewIfTaskIsCompleted(it)
        }
    }

    override fun onClick(view: View) {
        view.findNavController().navigate(
            TaskListFragmentDirections.actionTaskListFragmentToTaskFragment(task?.id)
        )
    }

    @ColorInt
    @Suppress("DEPRECATION")
    private fun getTitleColor(task: Task, resources: Resources): Int {
        return if (task.isCompleted) {
            resources.getColor(R.color.secondary_text)
        } else {
            resources.getColor(R.color.primary_text)
        }
    }

    @ColorInt
    @Suppress("DEPRECATION")
    private fun getDatetimeBarColor(task: Task, res: Resources): Int {
        val activeColor = res.getColor(R.color.primary_dark)
        val inactiveColor = res.getColor(R.color.secondary_text)
        val todayColor = res.getColor(R.color.accent_text_blue)
        val failedColor = res.getColor(R.color.red)
        return when {
            task.isCompleted -> inactiveColor
            task.date.isPast -> when {
                (task.date.isToday) && (task.isTimeSpecified != true) -> todayColor
                else -> failedColor
            }
            task.date.isToday -> todayColor
            else -> activeColor
        }
    }

    fun invalidateTask() {
        task = null
    }
}
