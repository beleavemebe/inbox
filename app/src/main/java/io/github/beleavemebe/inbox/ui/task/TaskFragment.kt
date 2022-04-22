package io.github.beleavemebe.inbox.ui.task

import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.PopupMenu
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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
import io.github.beleavemebe.inbox.databinding.FragmentTaskBinding
import io.github.beleavemebe.inbox.ui.DetailsFragment
import io.github.beleavemebe.inbox.ui.appComponent
import io.github.beleavemebe.inbox.ui.util.*
import kotlinx.coroutines.flow.onEach
import java.util.*
import javax.inject.Inject

class TaskFragment : DetailsFragment(R.layout.fragment_task) {
    private val args by navArgs<TaskFragmentArgs>()
    private val binding by viewBinding(FragmentTaskBinding::bind)

    @Inject lateinit var factory: TaskViewModel.Factory
    private val viewModel: TaskViewModel by assistedViewModel { handle ->
        factory.create(handle, args.taskId)
    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        subscribeToViewModel()
    }

    private fun setupUI() {
        initListeners()
        initChecklistRecycler()
        initTitleEditText()
    }

    private fun initListeners() {
        binding.btnSave.setOnClickListener { saveTask() }
        binding.cvDate.setOnClickListener { viewModel.pickDate() }
        binding.cvTime.setOnClickListener { viewModel.pickTime() }

        binding.etTitle.doOnTextChanged { text, _, _, _ ->
            binding.tiTitle.error = null
            viewModel.setTitle(text.toString())
        }

        binding.etNote.doOnTextChanged { text, _, _, _ ->
            viewModel.setNote(text.toString())
        }
    }

    private fun saveTask() {
        val text = binding.etTitle.text?.toString() ?: ""
        if (text.isBlank()) {
            binding.tiTitle.error = getString(R.string.task_is_blank)
        } else {
            viewModel.saveTask()
            findNavController().navigateUp()
        }
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

    private fun initTitleEditText() {
        binding.etTitle.enableDoneImeAction()
        if (!viewModel.isTaskIdGiven) {
            binding.etTitle.forceEditing()
        }
    }

    private fun subscribeToViewModel() {
        observeTask()
        observeSectionVisibility()
        observeEvents()
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
        findNavController().navigateUp()
    }

    private fun renderTask(task: Task) {
        binding.etTitle.setText(task.title)
        binding.etNote.setText(task.note)
        renderDatetimeSection(task)
        renderPeriodicitySection(task)
        renderChecklistSection(task)
        initSwitchListeners()
        binding.groupContent.isInvisible = false
        binding.progressBar.isVisible = false
    }

    private fun renderDatetimeSection(task: Task) {
        updateDateTv(task)
        updateTimeTv(task)
    }

    private fun updateDateTv(task: Task) {
        val dueDate = task.dueDate
        if (dueDate != null) {
            binding.tvDate.text = formatDueDate(dueDate)
        } else {
            binding.tvDate.text = ""
        }
    }

    private fun formatDueDate(date: Date) =
        DateFormat.format("EEE, d MMM yyyy", date).toString()
            .replaceFirstChar(Char::uppercase)

    private fun updateTimeTv(task: Task) {
        if (task.isTimeSpecified) {
            val cal = viewModel.calendar ?: return
            val hrs = cal.get(Calendar.HOUR_OF_DAY)
            val min = cal.get(Calendar.MINUTE)
            val time = "${hrs}:${min.toDoubleFiguredString()}"
            binding.tvTime.text = time
        } else {
            binding.tvTime.text = ""
        }
    }

    private fun renderPeriodicitySection(task: Task) {}

    private fun renderChecklistSection(task: Task) {
        val oldChecklist = task.checklist?.content?.map {
            ChecklistListItem.ChecklistEntry(it)
        }.orEmpty()
        val newItems =
            oldChecklist + ChecklistListItem.AddChecklistEntry(viewModel::addChecklistEntry)
        checklistAdapter.items = newItems
    }

    private fun initSwitchListeners() {
        binding.switchDatetime.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setDatetimeEnabled(isChecked)
        }

        binding.switchPeriodicity.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setPeriodicityEnabled(isChecked)
        }

        binding.switchChecklist.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setChecklistEnabled(isChecked)
        }
    }

    private fun observeSectionVisibility() {
        viewModel.isDatetimeSectionVisible
            .onEach(::setDatetimeSectionVisible)
            .launchWhenStarted(viewLifecycleOwner.lifecycleScope)

        viewModel.isPeriodicitySectionVisible
            .onEach(::setPeriodicitySectionVisible)
            .launchWhenStarted(viewLifecycleOwner.lifecycleScope)

        viewModel.isChecklistSectionVisible
            .onEach(::setChecklistSectionVisible)
            .launchWhenStarted(viewLifecycleOwner.lifecycleScope)
    }

    private fun setDatetimeSectionVisible(flag: Boolean) {
        binding.switchDatetime.isChecked = flag
        binding.groupDatetime.setVisibleAnimated(flag)
    }

    private fun setPeriodicitySectionVisible(flag: Boolean) {
        binding.switchPeriodicity.isChecked = flag
        binding.groupPeriodicity.setVisibleAnimated(flag)
    }

    private fun setChecklistSectionVisible(flag: Boolean) {
        binding.switchChecklist.isChecked = flag
        binding.groupChecklist.setVisibleAnimated(flag)
    }

    private fun observeEvents() {
        viewModel.eventShowPickDateMenu
            .onEach { showPickDatePopupMenu() }
            .launchWhenStarted(viewLifecycleOwner.lifecycleScope)

        viewModel.eventShowDatePickerDialog
            .onEach { showDatePicker(it) }
            .launchWhenStarted(viewLifecycleOwner.lifecycleScope)

        viewModel.eventShowTimePickerDialog
            .onEach { showTimePicker(it) }
            .launchWhenStarted(viewLifecycleOwner.lifecycleScope)

        viewModel.eventShowDateNotSetToast
            .onEach { context.toast(R.string.date_not_set) }
            .launchWhenStarted(viewLifecycleOwner.lifecycleScope)
    }

    private fun showPickDatePopupMenu() {
        PopupMenu(requireContext(), binding.cvDate)
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
        viewModel.showDatePicker()
    }

    private fun showDatePicker(date: Date? = null) {
        MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.select_date))
            .setSelection(date?.time ?: System.currentTimeMillis())
            .build()
            .apply {
                addOnPositiveButtonClickListener { ms -> viewModel.setDate(ms) }
            }.show(childFragmentManager, "MaterialDatePicker")
    }

    private fun showTimePicker(hourAndMinute: HourAndMinute) {
        MaterialTimePicker.Builder()
            .setTitleText(R.string.select_time)
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(hourAndMinute.hour)
            .setMinute(hourAndMinute.minute)
            .build()
            .apply {
                addOnNegativeButtonClickListener { viewModel.clearTime() }
                addOnPositiveButtonClickListener { viewModel.setTime(hour, minute) }
            }.show(childFragmentManager, "MaterialTimePicker")
    }
}
