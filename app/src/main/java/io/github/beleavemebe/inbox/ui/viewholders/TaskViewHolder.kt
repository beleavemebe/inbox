package io.github.beleavemebe.inbox.ui.viewholders

import android.graphics.Paint
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.databinding.ListItemTaskBinding
import io.github.beleavemebe.inbox.model.Task
import io.github.beleavemebe.inbox.repositories.TaskRepository
import io.github.beleavemebe.inbox.ui.fragments.tasklist.TaskListFragmentDirections
import io.github.beleavemebe.inbox.util.isToday
import io.github.beleavemebe.inbox.util.isTomorrow
import io.github.beleavemebe.inbox.util.isYesterday
import java.text.SimpleDateFormat
import java.util.*

const val TAG = "TaskViewHolder"

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
            taskTimeTv.text = resources.getString(R.string.task_datetime_placeholder, date, time)
        }
    }

    private fun alterViewIfTaskIsCompleted() = with (binding.taskTitleTv) {
        val textColor : Int
        if (task.isCompleted) {
            textColor = resources.getColor(R.color.secondary_text)
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            textColor = resources.getColor(R.color.primary_text)
            paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
        setTextColor(textColor)
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