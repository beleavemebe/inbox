package io.github.beleavemebe.inbox.ui.fragments.tasklist

import android.content.Context
import android.content.res.Resources
import android.text.format.DateFormat
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
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

    fun bind(task: Task) {
        this.task = task
        binding.initTitleTv(task)
        binding.initCompletedCb(task)
        binding.initDatetimeBar(task)
        binding.alterViewIfTaskIsCompleted(task)
    }

    private fun ListItemTaskBinding.initTitleTv(task: Task) {
        titleTv.text = task.title
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
        val taskDate = task.dueDate
        val resources = datetimeBar.resources
        datetimeBar.isVisible = taskDate != null
        if (taskDate != null) {
            taskTimeTv.text = getDatetimeText(task, resources)
            updateDatetimeBarColor(task, resources)
        }
    }

    private fun ListItemTaskBinding.alterViewIfTaskIsCompleted(task: Task) {
        titleTv.apply {
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

    private fun getDatetimeText(task: Task, resources: Resources): String {
        val dateText = when {
            task.dueDate.isYesterday -> resources.getString(R.string.yesterday)
            task.dueDate.isToday -> resources.getString(R.string.today)
            task.dueDate.isTomorrow -> resources.getString(R.string.tomorrow)
            else -> DateFormat
                .format("EEE, dd MMM", task.dueDate).toString()
                .replaceFirstChar { it.uppercase() }
        }

        val timeText = if (task.isTimeSpecified == true) {
            DateFormat.format("HH:mm", task.dueDate).toString()
        } else ""

        return resources.getString(R.string.task_datetime_placeholder, dateText, timeText)
    }

    init {
        binding.root.setOnClickListener(this)
    }

    private fun setTaskCompleted(flag: Boolean) {
        val task = task ?: return
        onTaskChecked(task.id, flag)
        binding.alterViewIfTaskIsCompleted(task)
    }

    override fun onClick(view: View) {
        val uuid = task?.id ?: return
        onTaskClicked(uuid)
    }

    private val appContext: Context
        get() = binding.root.context.applicationContext

    @ColorInt
    private fun getTitleColor(task: Task, res: Resources): Int {
        val textColorRes = if (task.isCompleted) R.color.secondary_text else R.color.primary_text
        return res.getColorCompat(appContext, textColorRes)
    }

    @ColorInt
    private fun getDatetimeBarColor(task: Task, res: Resources): Int {
        val activeColor = res.getColorCompat(appContext, R.color.primary_dark)
        val inactiveColor = res.getColorCompat(appContext, R.color.secondary_text)
        val todayColor = res.getColorCompat(appContext, R.color.accent_text_blue)
        val failedColor = res.getColorCompat(appContext, R.color.red)
        return when {
            task.isCompleted -> inactiveColor
            task.dueDate?.isPast == true -> if (task.isDueToday()) todayColor else failedColor
            else -> activeColor
        }
    }

    @ColorInt
    private fun Resources.getColorCompat(
        context: Context,
        @ColorRes colorResId: Int
    ): Int =
        ResourcesCompat.getColor(
            this,
            colorResId,
            context.applicationContext.theme
        )

    fun invalidateTask() {
        task = null
    }

    private fun Task.isDueToday() =
        dueDate.isToday && (isTimeSpecified?.not() == true)
}
