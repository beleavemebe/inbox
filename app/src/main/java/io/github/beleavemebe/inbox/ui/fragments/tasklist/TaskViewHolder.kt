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
import io.github.beleavemebe.inbox.databinding.ListItemTaskBinding
import io.github.beleavemebe.inbox.model.Task
import io.github.beleavemebe.inbox.repositories.TaskRepository
import io.github.beleavemebe.inbox.util.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*

class TaskViewHolder(
    private val binding: ListItemTaskBinding,
    private val onTaskClicked: (UUID) -> Unit,
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

    private fun getDatetimeText(task: Task, resources: Resources): String {
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
            task.date.isPast -> when {
                (task.date.isToday) && (task.isTimeSpecified != true) -> todayColor
                else -> failedColor
            }
            task.date.isToday -> todayColor
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
}
