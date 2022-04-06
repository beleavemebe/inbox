package io.github.beleavemebe.inbox.ui.task

import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.EditText
import android.widget.PopupMenu
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.core.common.util.HOUR_MS
import io.github.beleavemebe.inbox.core.model.CallResult
import io.github.beleavemebe.inbox.core.model.Task
import io.github.beleavemebe.inbox.core.model.getOrNull
import io.github.beleavemebe.inbox.databinding.FragmentTaskBinding
import io.github.beleavemebe.inbox.ui.appComponent
import io.github.beleavemebe.inbox.ui.DetailsFragment
import io.github.beleavemebe.inbox.ui.util.*
import kotlinx.coroutines.flow.onEach
import java.util.*
import javax.inject.Inject

class TaskFragment : DetailsFragment(R.layout.fragment_task) {
    private val args by navArgs<TaskFragmentArgs>()
    private val binding by viewBinding(FragmentTaskBinding::bind)

    @Inject lateinit var factory: TaskViewModel.Factory
    private val viewModel: TaskViewModel by assistedViewModel { ->
        factory.create(args.taskId)
    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeTask()
    }

    private fun setupUI() {
        initListeners()
        initChecklistRecycler()
        binding.etTitle.enableDoneImeAction()
        if (!viewModel.isTaskIdGiven) {
            binding.etTitle.forceEditing()
        }
        binding.scrollViewContent.setOnScrollChangeListener { _, _, _, _, _ ->
            activity?.currentFocus?.let {
                if (it == binding.etTitle || it == binding.etNote) {
                    it.clearFocus()
                    (it as EditText).hideKeyboard()
                }
            }
        }
    }

    private fun initListeners() {
        binding.btnSave.setOnClickListener(::saveTask)
        binding.cvTime.setOnClickListener(::showTimePicker)
        binding.cvDate.setOnClickListener(::showPickDatePopupMenu)
        binding.cvPeriodicity.setOnClickListener(::showPeriodicityDialog)
        binding.etTitle.doOnTextChanged { text, _, _, _ ->
            binding.tiTitle.error = null
            viewModel.setTitle(text.toString())
        }
        binding.etNote.doOnTextChanged { text, _, _, _ ->
            viewModel.setNote(text.toString())
        }
        binding.switchDatetime.setOnCheckedChangeListener { _, isChecked ->
            setDatetimeGroupVisible(isChecked)
            if (!isChecked) {
                viewModel.clearDatetime()
            }
        }
        binding.switchPeriodicity.setOnCheckedChangeListener { _, isChecked ->
            setPeriodicityGroupVisible(isChecked)
        }
    }

    private fun observeTask() {
        viewModel.taskFlow
            .onEach(::renderUi)
            .launchWhenStarted(viewLifecycleOwner.lifecycleScope)
    }

    private fun renderUi(result: CallResult<Task>) {
        when (result) {
            is CallResult.Loading -> renderLoading()
            is CallResult.Error -> renderError()
            is CallResult.Success -> renderTask(result.data)
        }
    }

    private fun renderLoading() {
        binding.groupContent.isInvisible = true
        binding.progressBar.isVisible = true
    }

    private fun renderError() {
        context.toast("Error")
    }

    private fun renderTask(task: Task) {
        activity?.currentFocus?.clearFocus()
        binding.etTitle.setText(task.title)
        binding.etNote.setText(task.note)
        initDatetimeSection(task)
        initPeriodicitySection(task)
        initChecklist(task)
        binding.groupContent.isInvisible = false
        binding.progressBar.isVisible = false
    }

    private fun showPeriodicityDialog(v: View) {
        // TODO: Periodicity dialog
    }

    private fun updateDateTv(task: Task) {
        binding.tvDate.text =
            task.dueDate?.let {
                formatDueDate(it)
            } ?: ""
    }

    private fun updateTimeTv(task: Task) {
        if (task.isTimeSpecified) {
            val cal = viewModel.calendar ?: return
            val hrs = cal.get(Calendar.HOUR_OF_DAY)
            val min = cal.get(Calendar.MINUTE)
            val time = "${hrs}:${min.toDoubleFiguredString()}"
            binding.tvTime.text = time
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
        viewModel.setDate(System.currentTimeMillis())
    }

    private fun onTomorrowOptionPicked() = true.also {
        viewModel.setDate(System.currentTimeMillis() + 24 * HOUR_MS)
    }

    private fun onPickDateOptionPicked() = true.also {
        showDatePicker()
    }

    private fun showDatePicker() {
        MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.select_date))
            .setSelection(viewModel.taskFlow.value.getOrNull()?.dueDate?.time ?: System.currentTimeMillis())
            .build()
            .apply {
                addOnPositiveButtonClickListener { ms -> viewModel.setDate(ms) }
            }.show(childFragmentManager, "MaterialDatePicker")
    }

    private fun showTimePicker(v: View?) {
        val cal = viewModel.calendar ?: return context.toast(R.string.date_not_set)
        var hrs = 12
        var min = 0
        if (viewModel.task?.isTimeSpecified ?: return) {
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
                addOnNegativeButtonClickListener { viewModel.clearTime() }
                addOnPositiveButtonClickListener { viewModel.setTime(hour, minute) }
            }.show(childFragmentManager, "MaterialTimePicker")
    }

    private fun initDatetimeSection(task: Task) {
        binding.switchDatetime.apply {
            isChecked = task.dueDate != null
            jumpDrawablesToCurrentState()
        }
        setDatetimeGroupVisible(task.dueDate != null)
        updateDateTv(task)
        updateTimeTv(task)
    }

    private fun setDatetimeGroupVisible(flag: Boolean) {
        binding.groupDatetime.setVisibleAnimated(flag)
    }

    private fun initPeriodicitySection(task: Task) {
        setPeriodicityGroupVisible(false)
    }

    private fun setPeriodicityGroupVisible(flag: Boolean) {
        binding.groupPeriodicity.setVisibleAnimated(flag)
    }

    private val checklistAdapter by lazy {
        AsyncListDifferDelegationAdapter(
            ChecklistListItem.DIFF_CALLBACK,
            ChecklistListItem.ChecklistEntry.delegate(
                viewModel::onChecklistItemTextChanged,
                viewModel::onChecklistItemChecked
            ),
            ChecklistListItem.AddChecklistEntry.delegate(),
        )
    }

    private fun initChecklistRecycler() {
        binding.rvChecklist.adapter = checklistAdapter
    }

    private fun initChecklist(task: Task) {
        val oldChecklist = task.checklist?.content?.map {
            ChecklistListItem.ChecklistEntry(it)
        }.orEmpty()
        val newItems = oldChecklist + ChecklistListItem.AddChecklistEntry(viewModel::addChecklistEntry)
        checklistAdapter.items = newItems
    }

    private fun saveTask(view: View) {
        val text = binding.etTitle.text?.toString() ?: ""
        if (text.isBlank()) {
            binding.tiTitle.error = getString(R.string.task_is_blank)
        } else {
            viewModel.saveTask()
            view.findNavController().navigateUp()
        }
    }

    private fun formatDueDate(date: Date) =
        DateFormat.format("EEE, d MMM yyyy", date).toString()
            .replaceFirstChar(Char::uppercase)

}
