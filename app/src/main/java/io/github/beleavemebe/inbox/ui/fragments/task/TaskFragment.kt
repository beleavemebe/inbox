package io.github.beleavemebe.inbox.ui.fragments.task

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.databinding.FragmentTaskBinding
import io.github.beleavemebe.inbox.model.Task
import io.github.beleavemebe.inbox.ui.activities.MainActivity.Companion.hideBottomNavMenu
import io.github.beleavemebe.inbox.ui.activities.MainActivity.Companion.mainToolbar
import io.github.beleavemebe.inbox.ui.activities.MainActivity.Companion.revealBottomNavMenu
import io.github.beleavemebe.inbox.ui.fragments.BaseFragment
import io.github.beleavemebe.inbox.util.HOUR_MS
import io.github.beleavemebe.inbox.util.forceEditing
import io.github.beleavemebe.inbox.util.toast
import java.lang.IllegalStateException
import java.util.*
import io.github.beleavemebe.inbox.util.calendar as extCalendar

class TaskFragment : BaseFragment(R.layout.fragment_task) {
    private val args: TaskFragmentArgs by navArgs()
    private val viewModel: TaskViewModel by viewModels()
    private val binding by viewBinding(FragmentTaskBinding::bind)

    private val task: Task
        get() = viewModel.task.value ?: throw IllegalStateException()

    private val calendar: Calendar?
        get() = task.date?.let {
            extCalendar.apply { time = it }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.taskId = args.taskId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavMenu()
        binding.setupUI()
        tweakToolbarTitle()
        observeTask()
    }

    private fun observeTask() {
        viewModel.task
            .observe(viewLifecycleOwner) { task: Task ->
                binding.updateUI()
            }
    }

    private fun tweakToolbarTitle() {
        mainToolbar.title =
            if (viewModel.isTaskIdGiven) {
                getString(R.string.task)
            } else {
                getString(R.string.new_task)
            }
    }

    private fun FragmentTaskBinding.setupUI() {
        initListeners()
        if (!viewModel.isTaskIdGiven) {
            hideTimestamp()
            titleEt.forceEditing()
        }
    }

    private fun FragmentTaskBinding.initListeners() {
        saveBtn.setOnClickListener(::saveTask)
        timeCv.setOnClickListener(::showTimePicker)
        dateCv.setOnClickListener(::showPickDatePopupMenu)
        periodicityCv.setOnClickListener(::showPeriodicityDialog)
        titleEt.doOnTextChanged { text, _, _, _ -> task.title = text.toString().trim() }
        noteEt.doOnTextChanged { text, _, _, _ -> task.note = text.toString().trim() }
    }

    private fun showPeriodicityDialog(v: View) {
        // TODO: Periodicity dialog
    }

    private fun FragmentTaskBinding.hideTimestamp() {
        timestampTv.visibility = View.INVISIBLE
    }

    private fun FragmentTaskBinding.updateDateTv() {
        dateTv.text =
            task.date?.let {
                viewModel.getFormattedDate(it)
            } ?: ""
    }

    private fun FragmentTaskBinding.updateTimeTv() {
        if (task.isTimeSpecified == true) {
            val cal = calendar ?: return
            val hrs = cal.get(Calendar.HOUR_OF_DAY)
            val min = cal.get(Calendar.MINUTE)
            val time = "${hrs}:${if (min >= 10) "$min" else "0$min"}"
            timeTv.text = time
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

    private fun onTodayOptionPicked() = true.also { setDate(System.currentTimeMillis()) }
    private fun onTomorrowOptionPicked() = true.also { setDate(System.currentTimeMillis() + 24 * HOUR_MS) }
    private fun onPickDateOptionPicked() = true.also { showDatePicker() }

    private fun showDatePicker() {
        MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.select_date))
            .setSelection(task.date?.time ?: System.currentTimeMillis())
            .build()
            .apply {
                addOnNegativeButtonClickListener { clearDate() }
                addOnPositiveButtonClickListener { ms -> setDate(ms) }
            }.show(childFragmentManager, "MaterialDatePicker")
    }

    private fun setDate(ms: Long) {
        val hrs = calendar?.get(Calendar.HOUR_OF_DAY) ?: 12
        val min = calendar?.get(Calendar.MINUTE) ?: 0
        task.date = Date(ms)
        binding.updateDateTv()
        if (task.isTimeSpecified == true) {
            setTime(hrs, min)
        }
    }

    private fun clearDate() {
        task.date = null
        binding.dateTv.text = ""
        clearTime()
    }

    private fun showTimePicker(`_`: View?) {
        val cal = calendar ?: return context.toast(R.string.date_not_set)
        var hrs = 12
        var min = 0
        if (task.isTimeSpecified == true) {
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
        task.date = calendar?.run {
            set(Calendar.HOUR_OF_DAY, pickedHour)
            set(Calendar.MINUTE, pickedMinute)
            time
        } ?: return
        task.isTimeSpecified = true
        binding.updateTimeTv()
    }

    private fun clearTime() {
        setTime(0, 0)
        task.isTimeSpecified = false
        binding.timeTv.text = ""
    }

    private fun FragmentTaskBinding.updateUI() {
        titleEt.setText(task.title)
        noteEt.setText(task.note)
        timestampTv.apply {
            text = getString(
                R.string.task_created_placeholder,
                viewModel.getFormattedTimestamp(task.timestamp)
            )
            visibility = View.VISIBLE
        }
        updateDateTv()
        updateTimeTv()
    }

    private fun saveTask(view: View) {
        if (task.isBlank()) {
            context.toast(R.string.task_is_blank)
        } else {
            viewModel.saveTask()
            navToTaskListFragment(view)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        revealBottomNavMenu()
    }

    private fun navToTaskListFragment(view: View) {
        view.findNavController()
            .navigate(
                R.id.action_taskFragment_to_taskListFragment
            )
    }

    private fun Task.isBlank() = title.isBlank()
}
