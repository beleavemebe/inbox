package io.github.beleavemebe.inbox.ui.fragments.task

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.databinding.FragmentTaskBinding
import io.github.beleavemebe.inbox.model.Task
import io.github.beleavemebe.inbox.util.*
import io.github.beleavemebe.inbox.util.calendar as extCalendar
import java.lang.IllegalArgumentException
import java.util.*

class TaskFragment : Fragment(R.layout.fragment_task) {
    private lateinit var task: Task
    private val taskViewModel: TaskViewModel by viewModels()
    private val binding: FragmentTaskBinding by viewBinding()

    private val calendar : Calendar?
        get() = task.date?.let {
            extCalendar.apply { time = it }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val taskId: UUID? = arguments?.get("taskId") as? UUID
        if (taskId == null) {
            task = Task()
            taskViewModel.onNoTaskIdGiven()
        } else {
            taskViewModel.onTaskIdGiven(taskId)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavMenu()
        setupUI()
        setTaskLiveDataObserver()
    }

    private fun setTaskLiveDataObserver() {
        taskViewModel.taskLiveData.observe(viewLifecycleOwner) { task: Task? ->
            this.task = task
                ?: throw IllegalArgumentException("Impossible wrong id")
            updateUI()
        }
    }

    private fun setupUI() {
        addTextWatchers()
        addNavListeners()
        addCheckboxListener()
        hideTimestamp()
        setHeaderText(R.string.new_task)
        initDatePickerListener()
        initTimePickerListener()
        if (!taskViewModel.isTaskIdGiven) showKeyboard()
    }

    private fun addTextWatchers() = with (binding) {
        TextWatcherImpl.newWatcher { sequence ->
            task.title = sequence.toString()
        }.also { titleTi.addTextChangedListener(it) }

        TextWatcherImpl.newWatcher { sequence ->
            task.note = sequence.toString()
        }.also { noteTi.addTextChangedListener(it) }
    }

    private fun addNavListeners() = with (binding) {
        backIb.setOnClickListener(::navToTaskListFragment)
        saveIb.setOnClickListener(::saveTask)
    }

    private fun addCheckboxListener() = with (binding) {
        doneCb.setOnCheckedChangeListener { _, isChecked ->
            if (taskViewModel.isTaskIdGiven) {
                task.isCompleted = isChecked
            } else {
                Toaster.get().toast(R.string.task_not_created)
                doneCb.apply {
                    this.isChecked = false
                    jumpDrawablesToCurrentState()
                }
            }
        }
    }

    private fun hideTimestamp() = with (binding) {
        timestampTv.visibility = View.INVISIBLE
    }

    private fun setHeaderText(@StringRes res: Int) = with (binding) {
        taskHeaderTv.text = getString(res)
    }

    private fun updateDateTv() = with (binding) {
        dateTv.text = task.date?.let { taskViewModel.getFormattedDate(it) } ?: ""
    }

    private fun updateTimeTv() = calendar?.run {
        if (task.isTimeSpecified == true) {
            val hrs = get(Calendar.HOUR_OF_DAY)
            val min = get(Calendar.MINUTE)
            binding.timeTv.text = time.run { "${hrs}:${if (min >= 10) "$min" else "0$min"}" }
        }
    }

    private fun initDatePickerListener() {
        binding.dateTv.setOnClickListener { showDatePicker() }
    }

    private fun initTimePickerListener() {
        binding.timeTv.setOnClickListener { showTimePicker() }
    }

    private fun showDatePicker() {
        MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.select_date))
            .setSelection(task.date?.time ?: System.currentTimeMillis())
            .build()
            .apply {
                addOnNegativeButtonClickListener { clearDate() }

                addOnPositiveButtonClickListener { ms ->
                    val hrs = calendar?.get(Calendar.HOUR_OF_DAY)
                    val min = calendar?.get(Calendar.MINUTE)
                    if (hrs != null && min != null) {
                        setDate(ms, hrs, min)
                    } else {
                        setDate(ms)
                    }
                }
            }.show(childFragmentManager, "MaterialDatePicker")
    }

    private fun setDate(ms: Long, hrs: Int = 0, minutes: Int = 0) {
        task.date = Date(
            ms + (hrs * HOUR_MS) + (minutes * MINUTE_MS)
        )
        updateDateTv()
    }

    private fun clearDate() {
        task.date = null
        binding.dateTv.text = ""
        clearTime()
    }

    private fun showTimePicker() {
        val cal = calendar ?: return Toaster.get().toast(R.string.date_not_set)
        val hrs = cal.get(Calendar.HOUR_OF_DAY)
        val min = cal.get(Calendar.MINUTE)
        MaterialTimePicker.Builder()
            .setTitleText(R.string.select_time)
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(hrs)
            .setMinute(min)
            .build()
            .apply {
                addOnPositiveButtonClickListener {
                    setTime(hour, minute)
                }
                addOnNegativeButtonClickListener {
                    clearTime()
                }
            }.show(childFragmentManager, "MaterialTimePicker")
    }

    private fun setTime(pickedHour: Int, pickedMinute: Int) {
        task.date = calendar?.run {
            set(Calendar.HOUR_OF_DAY, pickedHour)
            set(Calendar.MINUTE, pickedMinute)
            time
        } ?: return
        task.isTimeSpecified = true
        updateTimeTv()
    }

    private fun clearTime() = with (binding) {
        setTime(0, 0)
        task.isTimeSpecified = false
        binding.timeTv.text = ""
    }

    private fun showKeyboard() {
        binding.titleTi.requestFocus()
        (requireActivity()
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .showSoftInput(binding.titleTi, 0)
    }

    private fun updateUI() = with (binding) {
        setHeaderText(R.string.task)
        titleTi.setText(task.title)
        noteTi.setText(task.note)
        doneCb.apply {
            isEnabled = true
            isChecked = task.isCompleted
            jumpDrawablesToCurrentState()
        }
        timestampTv.apply {
            text = getString(
                R.string.task_timestamp,
                taskViewModel.getFormattedTimestamp(task.timestamp)
            )
            visibility = View.VISIBLE
        }
        updateDateTv()
        updateTimeTv()
    }

    private fun saveTask(view: View) {
        if (task.isBlank()) {
            Toaster.get().toast(R.string.task_is_blank)
        } else {
            taskViewModel.handleTask(task)
            navToTaskListFragment(view)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        revealBottomNavMenu()
    }

    private fun navToTaskListFragment(view: View) {
        Navigation.findNavController(view)
            .navigate(
                R.id.action_taskFragment_to_taskListFragment
            )
    }

    private fun Task.isBlank(): Boolean {
        return title == ""
    }
}
