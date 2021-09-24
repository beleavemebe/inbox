package io.github.beleavemebe.inbox.ui.adapters

import android.content.res.Resources
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.databinding.ListItemTaskBinding
import io.github.beleavemebe.inbox.model.Task
import io.github.beleavemebe.inbox.repositories.TaskRepository
import io.github.beleavemebe.inbox.ui.fragments.tasklist.TaskListFragmentDirections
import io.github.beleavemebe.inbox.util.*
import io.github.beleavemebe.inbox.util.isToday
import io.github.beleavemebe.inbox.util.isTomorrow
import io.github.beleavemebe.inbox.util.isYesterday
import java.text.SimpleDateFormat
import java.util.*

class TaskViewHolder(taskView: View) :
    RecyclerView.ViewHolder(taskView),
    View.OnClickListener
{
    lateinit var task: Task
    private val repo get() = TaskRepository.getInstance()
    private val binding: ListItemTaskBinding by viewBinding()

    fun bind(task: Task) {
        this.task = task
        initTitleTv(task)
        initCompletedCb(task)
        initDatetimeBar(task)
    }

    private fun initTitleTv(task: Task) = with (binding.taskTitleTv) {
        text = task.title
        alterViewIfTaskIsCompleted()
    }

    private fun initCompletedCb(task: Task) = with (binding) {
        completedCb.apply {
            isChecked = task.isCompleted
            jumpDrawablesToCurrentState()
            setOnCheckedChangeListener { _, isChecked ->
                task.isCompleted = isChecked
                repo.updateTask(task)
                alterViewIfTaskIsCompleted()
            }
        }
    }

    private fun initDatetimeBar(task: Task) = with (binding) {
        val taskDate = task.date
        val resources = datetimeBar.resources
        if (taskDate == null) {
            datetimeBar.isVisible = false
        } else {
            taskTimeTv.text = getDatetimeText(task.date!!, resources)
            updateDatetimeBarColor(resources)
        }
    }

    @Suppress("DEPRECATION")
    private fun alterViewIfTaskIsCompleted() = with (binding.taskTitleTv) {
        val titleTextColor = if (task.isCompleted) {
            resources.getColor(R.color.secondary_text)
        } else {
            resources.getColor(R.color.primary_text)
        }
        setCrossedOut(task.isCompleted)
        setTextColor(titleTextColor)
        updateDatetimeBarColor(resources)
    }

    private fun updateDatetimeBarColor(resources: Resources) = with (binding) {
        val datetimeBarColor = getDatetimeBarColor(resources)
        taskTimeTv.apply {
            setTextColor(datetimeBarColor)
            setCrossedOut(task.isCompleted)
        }
        taskTimeIv.setColorFilter(datetimeBarColor)
    }

    @Suppress("DEPRECATION")
    private fun getDatetimeBarColor(res: Resources): Int {
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

    private fun getDatetimeText(taskDate: Date, resources : Resources): String {
        val date = when {
            taskDate.isYesterday -> resources.getString(R.string.yesterday)
            taskDate.isToday -> resources.getString(R.string.today)
            taskDate.isTomorrow -> resources.getString(R.string.tomorrow)
            else -> SimpleDateFormat("EEE, dd MMM", Locale("ru"))
                .format(taskDate)
                .replaceFirstChar { it.uppercase() }
        }
        val time = if (task.isTimeSpecified == true) {
            SimpleDateFormat("HH:mm", Locale("ru")).format(taskDate)
        } else ""
        return resources.getString(R.string.task_datetime_placeholder, date, time)
    }

    init {
        taskView.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        view!!.findNavController().navigate(
                TaskListFragmentDirections.actionTaskListFragmentToTaskFragment(task.id)
            )
    }
}
