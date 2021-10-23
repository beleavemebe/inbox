package io.github.beleavemebe.inbox.ui.fragments.task

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
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
    private val viewModel: TaskViewModel by viewModels()
    private val binding by viewBinding(FragmentTaskBinding::bind)

    private val calendar: Calendar?
        get() = task.date?.let {
            extCalendar.apply { time = it }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val taskId: UUID? = arguments?.get("taskId") as? UUID
        if (taskId == null) {
            task = Task()
            viewModel.onNoTaskIdGiven()
        } else {
            viewModel.onTaskIdGiven(taskId)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavMenu()
        setupUI()
        setTaskLiveDataObserver()
    }

    private fun setTaskLiveDataObserver() {
        viewModel.taskLiveData.observe(viewLifecycleOwner) { task: Task? ->
            this.task = task
                ?: throw IllegalArgumentException("Impossible wrong id")
            updateUI()
        }
    }

    private fun setupUI() {
        addTextWatchers()
        addNavListeners()
        initDatePickerListener()
        initTimePickerListener()
        if (!viewModel.isTaskIdGiven) {
            setHeaderText(R.string.new_task)
            hideTimestamp()
            forceEditTitle()
        } else {
            setHeaderText(R.string.task)
        }
    }

    private fun addTextWatchers() = with (binding) {
        titleEt.addOnTextChangedListener { text -> task.title = text.toString().trim() }
        noteEt.addOnTextChangedListener { text -> task.note = text.toString().trim() }
    }

    private fun addNavListeners() = with (binding) {
        saveBtn.setOnClickListener(::saveTask)
//        non-existent
//        backIb.setOnClickListener(::navToTaskListFragment)
    }

    private fun setHeaderText(@StringRes res: Int) = with (binding) {
//        non-existent
//        taskHeaderTv.text = getString(res)
    }

    private fun hideTimestamp() = with (binding) {
        timestampTv.visibility = View.INVISIBLE
    }

    private fun forceEditTitle() {
        binding.titleEt.requestFocus()
        (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .showSoftInput(binding.titleEt, 0)
    }

    private fun updateDateTv() = with (binding) {
        val date: String = task.date?.let { viewModel.getFormattedDate(it) } ?: ""
        dateTv.text = date
    }

    private fun updateTimeTv() = calendar?.run {
        if (task.isTimeSpecified == true) {
            val hrs = get(Calendar.HOUR_OF_DAY)
            val min = get(Calendar.MINUTE)
            val time: String = time.run { "${hrs}:${if (min >= 10) "$min" else "0$min"}" }
            binding.timeTv.text = time
        }
    }

    private fun initDatePickerListener() {
        binding.dateCv.setOnClickListener(::showPickDateMenu)
    }

    private fun showPickDateMenu(view: View) {
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

    private fun onTodayOptionPicked()    = true.also { setDate(System.currentTimeMillis()) }
    private fun onTomorrowOptionPicked() = true.also { setDate(System.currentTimeMillis() + 24*HOUR_MS) }
    private fun onPickDateOptionPicked() = true.also { showDatePicker() }

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
                    setDate(ms)
                    if (task.isTimeSpecified == true && hrs != null && min != null) {
                        setTime(hrs, min)
                    }
                }
            }.show(childFragmentManager, "MaterialDatePicker")
    }

    private fun setDate(ms: Long) {
        task.date = Date(ms)
        updateDateTv()
    }

    private fun clearDate() {
        task.date = null
        binding.dateTv.text = ""
        clearTime()
    }

    private fun initTimePickerListener() {
        binding.timeCv.setOnClickListener { showTimePicker() }
    }

    private fun showTimePicker() {
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
        updateTimeTv()
    }

    private fun clearTime() = with (binding) {
        setTime(0, 0)
        task.isTimeSpecified = false
        binding.timeTv.text = ""
    }

    private fun updateUI() = with (binding) {
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
            viewModel.handleTask(task)
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

    private fun Task.isBlank() = title.isBlank()
}
