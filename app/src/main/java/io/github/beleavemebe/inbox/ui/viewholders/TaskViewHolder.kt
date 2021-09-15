package io.github.beleavemebe.inbox.ui.viewholders

import android.content.res.Resources
import android.graphics.Color
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
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
    private var binding: ListItemTaskBinding = ListItemTaskBinding.bind(taskView)

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

    private fun alterViewIfTaskIsCompleted() = with (binding.taskTitleTv) {
        val textColor : Int
        if (task.isCompleted) {
            textColor = resources.getColor(R.color.secondary_text)
        } else {
            textColor = resources.getColor(R.color.primary_text)
        }
        setCrossedOut(task.isCompleted)
        setTextColor(textColor)
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

    private fun getDatetimeBarColor(res: Resources): Int =
        when {
            task.isCompleted -> res.getColor(R.color.secondary_text)
            System.currentTimeMillis() > (task.date?.time ?: 0L) -> Color.RED
            task.date?.isToday ?: false -> res.getColor(R.color.accent_text_blue)
            else -> res.getColor(R.color.primary_dark)
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
