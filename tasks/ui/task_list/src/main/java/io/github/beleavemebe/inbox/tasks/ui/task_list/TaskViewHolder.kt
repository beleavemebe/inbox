package io.github.beleavemebe.inbox.tasks.ui.task_list

import android.content.Context
import android.content.res.Resources
import android.text.format.DateFormat
import android.text.format.DateUtils
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import io.github.beleavemebe.inbox.common.DAY_MS
import io.github.beleavemebe.inbox.common.isPast
import io.github.beleavemebe.inbox.core.utils.*
import io.github.beleavemebe.inbox.tasks.domain.model.Task
import io.github.beleavemebe.inbox.tasks.ui.task_list.databinding.ListItemTaskBinding
import java.util.*

class TaskViewHolder(
    private val binding: ListItemTaskBinding,
    private val onTaskClicked: (UUID) -> Unit,
    private val onTaskChecked: (UUID, Boolean) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    var task: Task? = null

    private val context: Context
        get() = binding.root.context

    fun bind(task: Task) {
        this.task = task
        initTitleTv(task)
        initCompletedCb(task)
        initDatetimeBar(task)
        initListeners(task.id)
        alterViewIfTaskIsCompleted(task)
    }

    private fun initTitleTv(task: Task) {
        binding.tvTitle.text = task.title
    }

    private fun initCompletedCb(task: Task) {
        binding.cbCompleted.setOnCheckedChangeListener(null)
        binding.cbCompleted.isChecked = task.isCompleted
        binding.cbCompleted.setOnCheckedChangeListener { _, isChecked ->
            log("Checked task ${task.title}")
            onTaskChecked(task.id, isChecked)
        }
    }

    private fun initDatetimeBar(task: Task) {
        val taskDate = task.dueDate
        val resources = binding.datetimeBar.resources
        binding.datetimeBar.isVisible = taskDate != null
        if (taskDate != null) {
            binding.tvTaskDatetime.text = getDatetimeText(task, resources)
            updateDatetimeBarColor(task, resources)
        }
    }

    private fun updateDatetimeBarColor(task: Task, res: Resources) {
        val datetimeBarColor = getDatetimeBarColor(task, res, context)
        binding.ivCalendar.setColorFilter(datetimeBarColor)
        binding.tvTaskDatetime.setTextColor(datetimeBarColor)
        binding.tvTaskDatetime.setCrossedOut(task.isCompleted)
    }

    private fun initListeners(id: UUID) {
        binding.root.setOnClickListener {
            onTaskClicked(id)
        }
    }

    private fun alterViewIfTaskIsCompleted(task: Task) {
        val titleTextColor = getTitleColor(task, binding.tvTitle.resources)
        binding.tvTitle.setCrossedOut(task.isCompleted)
        binding.tvTitle.setTextColor(titleTextColor)
    }

    @ColorInt
    private fun getTitleColor(task: Task, res: Resources): Int {
        val textColorRes = if (task.isCompleted) {
            R.color.task_item_title_completed
        } else {
            R.color.task_item_title
        }
        return res.getColorCompat(context, textColorRes)
    }
}

const val DUE_DATE_FORMAT = "EEE, dd MMM"
const val TIME_FORMAT = "HH:mm"

fun getDatetimeText(task: Task, resources: Resources): String {
    val dateText = when {
        task.dueDate.isYesterday -> resources.getString(R.string.yesterday)
        task.dueDate.isToday -> resources.getString(R.string.today)
        task.dueDate.isTomorrow -> resources.getString(R.string.tomorrow)
        else -> DateFormat
            .format(DUE_DATE_FORMAT, task.dueDate).toString()
            .replaceFirstChar(Char::uppercase)
    }

    val timeText = if (task.isTimeSpecified) {
        DateFormat.format(TIME_FORMAT, task.dueDate).toString()
    } else ""

    return resources.getString(R.string.task_datetime_placeholder, dateText, timeText)
}

@ColorInt
fun getDatetimeBarColor(task: Task, res: Resources, context: Context): Int {
    fun color(@ColorRes id: Int) = res.getColorCompat(context, id)

    val activeColor by lazy { color(R.color.task_item_datetime_active) }
    val inactiveColor by lazy { color(R.color.task_item_datetime_inactive) }
    val todayColor by lazy { color(R.color.task_item_datetime_today) }
    val failedColor by lazy { color(R.color.error) }
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

val Date?.isToday get() =
    this?.let { DateUtils.isToday(it.time) } ?: false

val Date?.isYesterday get() =
    this?.let { Date(it.time + DAY_MS).isToday } ?: false

val Date?.isTomorrow get() =
    this?.let { Date(it.time - DAY_MS).isToday } ?: false
