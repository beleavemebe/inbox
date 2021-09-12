package io.github.beleavemebe.inbox.ui.fragments.task

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.databinding.FragmentTaskBinding
import io.github.beleavemebe.inbox.model.Task
import io.github.beleavemebe.inbox.util.TextWatcherImpl
import io.github.beleavemebe.inbox.util.Toaster
import io.github.beleavemebe.inbox.util.hideBottomNavMenu
import io.github.beleavemebe.inbox.util.revealBottomNavMenu
import java.lang.IllegalArgumentException
import java.util.*

class TaskFragment : Fragment(R.layout.fragment_task) {
    private lateinit var task : Task
    private val taskViewModel: TaskViewModel by viewModels()

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val taskId : UUID? = arguments?.get("taskId") as? UUID
        if (taskId == null) {
            task = Task()
            taskViewModel.onNoTaskIdGiven()
        } else {
            taskViewModel.onTaskIdGiven(taskId)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTaskBinding.bind(view)

        hideBottomNavMenu()
        setupUI()
        setTaskLiveDataObserver()
    }

    private fun setTaskLiveDataObserver() {
        taskViewModel.taskLiveData.observe(viewLifecycleOwner) { task : Task? ->
            this.task = task ?: throw IllegalArgumentException("Impossible wrong id")
            updateUI()
        }
    }

    private fun setupUI() {
        addTextWatchers()
        addListeners()
        addCheckboxListener()
        hideTimestamp()
        setHeaderText(R.string.new_task)
        initDateTimePicker()
    }

    private fun initDateTimePicker() {
        binding.dateTv.setOnClickListener { showDatePicker() }
    }

    private fun showDatePicker() {
        MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.select_date))
            .setSelection(task.date?.time ?: System.currentTimeMillis())
            .build()
            .apply {
                addOnNegativeButtonClickListener {
                    clearDateTv()
                }
                addOnPositiveButtonClickListener { ms ->
                    val pickedDate = Date(ms)
                    updateDate(pickedDate)
                    showTimePicker()
                }
            }.show(childFragmentManager, "MaterialDatePicker")

    }

    private fun updateDate(pickedDate: Date) {
        task.date = pickedDate
        binding.dateTv.text = taskViewModel.getFormattedDate(pickedDate)
    }

    private fun clearDateTv() {
        task.date = null
        binding.dateTv.text = ""
    }

    private fun showTimePicker() {
        MaterialTimePicker.Builder()
            .setTitleText(R.string.select_time)
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(12)
            .setMinute(0)
            .build()
            .apply {
                addOnPositiveButtonClickListener {
                    updateTime(hour, minute)
                }
            }.show(childFragmentManager, "MaterialTimePicker")
    }

    private fun updateTime(pickedHour: Int, pickedMinute: Int) {
        task.date?.hours = pickedHour
        task.date?.minutes = pickedMinute
        updateDateTv()
    }

    private fun updateUI() = with (binding) {
        setHeaderText(R.string.task)
        titleTi.setText(task.title)
        noteTi.setText(task.note)
        doneCb.apply {
            isEnabled  = true
            isSelected = true
            isChecked  = task.isCompleted
            jumpDrawablesToCurrentState()
        }
        timestampTv.apply {
            text = getString(R.string.task_timestamp, taskViewModel.getFormattedTimestamp(task.timestamp))
            visibility = View.VISIBLE
        }
        updateDateTv()
    }

    private fun updateDateTv() = with (binding) {
        dateTv.text = task.date?.let { taskViewModel.getFormattedDate(it) } ?: ""
    }

    private fun setHeaderText(@StringRes res: Int) = with (binding) {
        taskHeaderTv.text = getString(res)
    }

    private fun saveTask(view: View) {
        if (task.isBlank()) {
            Toaster.get().toast(R.string.task_is_blank)
        } else {
            taskViewModel.onExitFragment(task)
            navToTaskListFragment(view)
        }
    }

    private fun navToTaskListFragment(view: View) {
        Navigation.findNavController(view)
            .navigate(
                R.id.action_taskFragment_to_taskListFragment
            )
    }

    private fun addTextWatchers() = with (binding) {
        TextWatcherImpl.newWatcher { sequence ->
            task.title = sequence.toString()
        }.also { titleTi.addTextChangedListener(it) }

        TextWatcherImpl.newWatcher { sequence ->
            task.note = sequence.toString()
        }.also { noteTi.addTextChangedListener(it) }
    }

    private fun addListeners() = with (binding) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        revealBottomNavMenu()
        _binding = null
    }

    private fun Task.isBlank() : Boolean {
        return title == ""
    }
}
