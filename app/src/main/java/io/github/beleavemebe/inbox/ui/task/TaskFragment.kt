package io.github.beleavemebe.inbox.ui.task

import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.PopupMenu
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.core.common.util.HOUR_MS
import io.github.beleavemebe.inbox.core.common.util.calendar
import io.github.beleavemebe.inbox.core.model.Task
import io.github.beleavemebe.inbox.databinding.FragmentTaskBinding
import io.github.beleavemebe.inbox.ui.appComponent
import io.github.beleavemebe.inbox.ui.DetailsFragment
import io.github.beleavemebe.inbox.ui.util.enableDoneImeAction
import io.github.beleavemebe.inbox.ui.util.forceEditing
import io.github.beleavemebe.inbox.ui.util.setVisibleAnimated
import io.github.beleavemebe.inbox.ui.util.toast
import java.util.*
import javax.inject.Inject

class TaskFragment : DetailsFragment(R.layout.fragment_task) {
    private val args by navArgs<TaskFragmentArgs>()
    private val binding by viewBinding(FragmentTaskBinding::bind)

    @Inject lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModels<TaskViewModel> { factory }

    private val task: Task
        get() = viewModel.task.value ?: throw IllegalStateException()

    private val calendar: Calendar?
        get() = task.dueDate?.calendar()

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.taskId.value = args.taskId
        setupUI()
        observeTask()
    }

    private fun setupUI() {
        initListeners()
        binding.titleEt.enableDoneImeAction()
        if (!viewModel.isTaskIdGiven) {
            hideTimestamp()
            binding.titleEt.forceEditing()
        }
    }

    private fun initListeners() {
        binding.saveBtn.setOnClickListener(::saveTask)
        binding.timeCv.setOnClickListener(::showTimePicker)
        binding.dateCv.setOnClickListener(::showPickDatePopupMenu)
        binding.periodicityCv.setOnClickListener(::showPeriodicityDialog)
        binding.titleEt.doOnTextChanged { text, _, _, _ ->
            binding.titleTi.error = null
            task.title = text.toString().trim()
        }
        binding.noteEt.doOnTextChanged { text, _, _, _ ->
            task.note = text.toString().trim()
        }
        binding.datetimeSwitch.setOnCheckedChangeListener { _, isChecked ->
            setDatetimeGroupVisible(isChecked)
            if (!isChecked) {
                clearDatetime()
            }
        }
        binding.periodicitySwitch.setOnCheckedChangeListener { _, isChecked ->
            setPeriodicityGroupVisible(isChecked)
        }
    }

    private fun observeTask() {
        viewModel.task.observe(viewLifecycleOwner) {
            updateUI()
        }
    }

    private fun updateUI() {
        binding.titleEt.setText(task.title)
        binding.noteEt.setText(task.note)
        initDatetimeSection()
        initPeriodicitySection()
        if (viewModel.isTaskIdGiven) {
            initTimestampTv()
        }
    }

    private fun showPeriodicityDialog(v: View) {
        // TODO: Periodicity dialog
    }

    private fun hideTimestamp() {
        binding.timestampTv.visibility = View.INVISIBLE
    }

    private fun updateDateTv() {
        binding.dateTv.text =
            task.dueDate?.let {
                formatDueDate(it)
            } ?: ""
    }

    private fun updateTimeTv() {
        if (task.isTimeSpecified) {
            val cal = calendar ?: return
            val hrs = cal.get(Calendar.HOUR_OF_DAY)
            val min = cal.get(Calendar.MINUTE)
            val time = "${hrs}:${if (min >= 10) "$min" else "0$min"}"
            binding.timeTv.text = time
        }
    }

    private fun showPickDatePopupMenu(view: View) {
        PopupMenu(requireContext(), view)
            .apply {
                menuInflater.inflate(R.menu.menu_pick_date, menu)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.option_pick_date_today -> onTodayOptionPicked()
                        R.id.option_pick_date_tomorrow -> onTomorrowOptionPicked()
                        R.id.option_pick_date_select_date -> onPickDateOptionPicked()
                        else -> false
                    }
                }
            }.show()
    }

    private fun onTodayOptionPicked() = true.also {
        setDate(System.currentTimeMillis())
    }

    private fun onTomorrowOptionPicked() = true.also {
        setDate(System.currentTimeMillis() + 24 * HOUR_MS)
    }

    private fun onPickDateOptionPicked() = true.also {
        showDatePicker()
    }

    private fun showDatePicker() {
        MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.select_date))
            .setSelection(task.dueDate?.time ?: System.currentTimeMillis())
            .build()
            .apply {
                addOnNegativeButtonClickListener { clearDatetime() }
                addOnPositiveButtonClickListener { ms -> setDate(ms) }
            }.show(childFragmentManager, "MaterialDatePicker")
    }

    private fun setDate(ms: Long) {
        val hrs = calendar?.get(Calendar.HOUR_OF_DAY) ?: 12
        val min = calendar?.get(Calendar.MINUTE) ?: 0
        task.dueDate = Date(ms)
        updateDateTv()
        if (task.isTimeSpecified) {
            setTime(hrs, min)
        }
    }

    private fun clearDatetime() {
        clearDate()
        clearTime()
    }

    private fun clearDate() {
        task.dueDate = null
        binding.dateTv.text = ""
    }

    private fun showTimePicker(v: View?) {
        val cal = calendar ?: return context.toast(R.string.date_not_set)
        var hrs = 12
        var min = 0
        if (task.isTimeSpecified) {
            hrs = cal.get(Calendar.HOUR_OF_DAY)
            min = cal.get(Calendar.MINUTE)
        }

        MaterialTimePicker.Builder()
            .setTitleText(R.string.select_time)
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(hrs)
            .setMinute(min)
            .build()
            .apply {
                addOnNegativeButtonClickListener { clearTime() }
                addOnPositiveButtonClickListener { setTime(hour, minute) }
            }.show(childFragmentManager, "MaterialTimePicker")
    }

    private fun setTime(pickedHour: Int, pickedMinute: Int) {
        task.dueDate = calendar?.run {
            set(Calendar.HOUR_OF_DAY, pickedHour)
            set(Calendar.MINUTE, pickedMinute)
            time
        } ?: return
        task.isTimeSpecified = true
        updateTimeTv()
    }

    private fun clearTime() {
        setTime(0, 0)
        task.isTimeSpecified = false
        binding.timeTv.text = ""
    }

    private fun initTimestampTv() {
        binding.timestampTv.apply {
            text = getString(
                R.string.task_created_placeholder,
                formatTimestamp(task.timestamp)
            )
            visibility = View.VISIBLE
        }
    }

    private fun initDatetimeSection() {
        binding.datetimeSwitch.apply {
            isChecked = task.dueDate != null
            jumpDrawablesToCurrentState()
        }
        setDatetimeGroupVisible(task.dueDate != null)
        updateDateTv()
        updateTimeTv()
    }

    private fun setDatetimeGroupVisible(flag: Boolean) {
        binding.datetimeGroup.setVisibleAnimated(flag)
    }

    private fun initPeriodicitySection() {
        setPeriodicityGroupVisible(false)
    }

    private fun setPeriodicityGroupVisible(flag: Boolean) {
        binding.periodicityGroup.setVisibleAnimated(flag)
    }

    private fun saveTask(view: View) {
        if (task.isBlank()) {
            binding.titleTi.error = getString(R.string.task_is_blank)
        } else {
            viewModel.saveTask()
            view.findNavController().navigateUp()
        }
    }

    private fun formatDueDate(date: Date) =
        DateFormat.format("EEE, d MMM yyyy", date).toString()
            .replaceFirstChar(Char::uppercase)

    private fun formatTimestamp(date: Date) =
        DateFormat.format("dd MMM `yy HH:mm", date).toString()

    private fun Task.isBlank() = title.isBlank()
}
