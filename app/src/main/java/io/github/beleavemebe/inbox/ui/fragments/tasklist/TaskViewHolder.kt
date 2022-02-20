package io.github.beleavemebe.inbox.ui.fragments.tasklist

import android.content.Context
import android.content.res.Resources
import android.text.format.DateFormat
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.core.common.util.isPast
import io.github.beleavemebe.inbox.core.model.Task
import io.github.beleavemebe.inbox.databinding.ListItemTaskBinding
import io.github.beleavemebe.inbox.ui.util.*
import java.util.*

class TaskViewHolder(
    private val binding: ListItemTaskBinding,
    private val onTaskClicked: (UUID) -> Unit,
    private val onTaskChecked: (UUID, Boolean) -> Unit,
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
    var task: Task? = null
        private set

    private val context: Context
        get() = binding.root.context.applicationContext

    fun bind(task: Task) {
        this.task = task
        initTitleTv(task)
        initCompletedCb(task)
        initDatetimeBar(task)
        alterViewIfTaskIsCompleted(task)
    }

    private fun initTitleTv(task: Task) {
        binding.titleTv.text = task.title
    }

    private fun initCompletedCb(task: Task) {
        binding.completedCb.apply {
            isChecked = task.isCompleted
            setOnCheckedChangeListener { _, isChecked ->
                setTaskCompleted(isChecked)
            }
        }
    }

    private fun initDatetimeBar(task: Task) {
        val taskDate = task.dueDate
        val resources = binding.datetimeBar.resources
        binding.datetimeBar.isVisible = taskDate != null
        if (taskDate != null) {
            binding.taskTimeTv.text = getDatetimeText(task, resources)
            updateDatetimeBarColor(task, resources)
        }
    }

    private fun alterViewIfTaskIsCompleted(task: Task) {
        binding.titleTv.apply {
            val titleTextColor = getTitleColor(task, resources)
            setCrossedOut(task.isCompleted)
            setTextColor(titleTextColor)
            updateDatetimeBarColor(task, resources)
        }
    }

    private fun updateDatetimeBarColor(task: Task, res: Resources) {
        val datetimeBarColor = getDatetimeBarColor(task, res, context)
        binding.taskTimeIv.setColorFilter(datetimeBarColor)
        binding.taskTimeTv.apply {
            setTextColor(datetimeBarColor)
            setCrossedOut(task.isCompleted)
        }
    }

    init {
        binding.root.setOnClickListener(this)
    }

    private fun setTaskCompleted(flag: Boolean) {
        val task = task ?: return
        onTaskChecked(task.id, flag)
        alterViewIfTaskIsCompleted(task)
    }

    override fun onClick(view: View) {
        val uuid = task?.id ?: return
        onTaskClicked(uuid)
    }

    @ColorInt
    private fun getTitleColor(task: Task, res: Resources): Int {
        val textColorRes = if (task.isCompleted) R.color.secondary_text else R.color.primary_text
        return res.getColorCompat(context, textColorRes)
    }

    fun invalidateTask() {
        task = null
    }
}

fun getDatetimeText(task: Task, resources: Resources): String {
    val dateText = when {
        task.dueDate.isYesterday -> resources.getString(R.string.yesterday)
        task.dueDate.isToday -> resources.getString(R.string.today)
        task.dueDate.isTomorrow -> resources.getString(R.string.tomorrow)
        else -> DateFormat
            .format("EEE, dd MMM", task.dueDate).toString()
            .replaceFirstChar { it.uppercase() }
    }

    val timeText = if (task.isTimeSpecified) {
        DateFormat.format("HH:mm", task.dueDate).toString()
    } else ""

    return resources.getString(R.string.task_datetime_placeholder, dateText, timeText)
}

@ColorInt
fun getDatetimeBarColor(task: Task, res: Resources, context: Context): Int {
    fun color(@ColorRes id: Int) = res.getColorCompat(context, id)

    val activeColor = color(R.color.primary_dark)
    val inactiveColor = color(R.color.secondary_text)
    val todayColor = color(R.color.accent_text_blue)
    val failedColor = color(R.color.red)
    val dueDate = task.dueDate
    return when {
        task.isCompleted -> inactiveColor
        dueDate?.isPast ?: false -> when {
            dueDate.isToday && !task.isTimeSpecified -> todayColor
            else -> failedColor
        }
        dueDate.isToday -> todayColor
        else -> activeColor
    }
}
