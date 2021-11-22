package io.github.beleavemebe.inbox.ui.fragments.task

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.databinding.FragmentTaskBinding
import io.github.beleavemebe.inbox.model.Task
import io.github.beleavemebe.inbox.ui.fragments.BaseFragment
import io.github.beleavemebe.inbox.util.*
import io.github.beleavemebe.inbox.util.calendar as extCalendar
import java.lang.IllegalArgumentException
import java.util.*

class TaskFragment :
    BaseFragment(R.layout.fragment_task)
{
    private val viewModel: TaskViewModel by viewModels()
    private val binding by viewBinding(FragmentTaskBinding::bind)

    private lateinit var task: Task

    private val calendar: Calendar?
        get() = task.date?.let {
            extCalendar.apply { time = it }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.taskId = getTypedArg<UUID>("taskId")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        hideBottomNavMenu()
        setTaskLiveDataObserver()
    }

    override fun adaptMainToolbar() {
        with(mainToolbar) {
            navigationIcon = drawable(R.drawable.ic_baseline_arrow_back_24)
            setNavigationIconTint(Color.WHITE)
            setNavigationOnClickListener { binding.root.findNavController().navigateUp() }
            title = if (viewModel.isTaskIdGiven) {
                getString(R.string.task)
            } else {
                getString(R.string.new_task)
            }
        }
    }

    private fun setTaskLiveDataObserver() {
        viewModel.taskLiveData
            .observe(viewLifecycleOwner) { task: Task? ->
                this.task = task
                    ?: throw IllegalArgumentException("Impossible wrong id")
                updateUI()
            }
    }

    private fun setupUI() {
        initListeners()
        if (!viewModel.isTaskIdGiven) {
            hideTimestamp()
            binding.titleEt.forceEditing()
        }
    }

    private fun initListeners() {
        with(binding) {
            saveBtn.setOnClickListener(::saveTask)
            dateCv.setOnClickListener(::showPickDateMenu)
            timeCv.setOnClickListener { showTimePicker() }
            titleEt.doOnTextChanged { text, _, _, _  -> task.title = text.toString().trim() }
            noteEt.doOnTextChanged { text, _, _, _ -> task.note = text.toString().trim() }
        }
    }


    private fun hideTimestamp() {
        with(binding) {
            timestampTv.visibility = View.INVISIBLE
        }
    }

    private fun updateDateTv() {
        with(binding) {
            val date: String = task.date?.let { viewModel.getFormattedDate(it) } ?: ""
            dateTv.text = date
        }
    }

    private fun updateTimeTv() = calendar?.run {
        if (task.isTimeSpecified == true) {
            val hrs = get(Calendar.HOUR_OF_DAY)
            val min = get(Calendar.MINUTE)
            val time: String = time.run { "${hrs}:${if (min >= 10) "$min" else "0$min"}" }
            binding.timeTv.text = time
        }
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
                addOnPositiveButtonClickListener { setTime(hour, minute) }
                addOnNegativeButtonClickListener { clearTime() }
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

    private fun clearTime() {
        with(binding) {
            setTime(0, 0)
            task.isTimeSpecified = false
            timeTv.text = ""
        }
    }

    private fun updateUI() {
        with(binding) {
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
        view.findNavController()
            .navigate(
                R.id.action_taskFragment_to_taskListFragment
            )
    }

    private fun Task.isBlank() = title.isBlank()
}
