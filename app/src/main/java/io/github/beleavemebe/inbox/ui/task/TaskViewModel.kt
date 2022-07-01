package io.github.beleavemebe.inbox.ui.task

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.domain.common.util.calendar
import io.github.beleavemebe.inbox.domain.model.*
import io.github.beleavemebe.inbox.domain.usecase.AddTask
import io.github.beleavemebe.inbox.domain.usecase.GetTaskById
import io.github.beleavemebe.inbox.domain.usecase.UpdateTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import java.util.function.Function

class TaskViewModel @AssistedInject constructor(
    @Assisted private val taskId: UUID? = null,
    @Assisted private val handle: SavedStateHandle,
    private val getTaskById: GetTaskById,
    private val addTask: AddTask,
    private val updateTask: UpdateTask,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(handle: SavedStateHandle, taskId: UUID?): TaskViewModel
    }

    private val _taskFlow = MutableStateFlow<CallResult<Task>>(CallResult.Loading)
    val taskFlow = _taskFlow.asStateFlow()

    private val _isDatetimeSectionVisible = MutableStateFlow(false)
    val isDatetimeSectionVisible = _isDatetimeSectionVisible.asStateFlow()

    private val _isPeriodicitySectionVisible = MutableStateFlow(false)
    val isPeriodicitySectionVisible = _isPeriodicitySectionVisible.asStateFlow()

    private val _isChecklistSectionVisible = MutableStateFlow(false)
    val isChecklistSectionVisible = _isChecklistSectionVisible.asStateFlow()

    val task: Task?
        get() = taskFlow.value.getOrNull()

    val calendar: Calendar?
        get() = task?.dueDate?.calendar()

    private val _eventShowPickDateMenu = MutableSharedFlow<Boolean>()
    val eventShowPickDateMenu = _eventShowPickDateMenu.asSharedFlow()

    private val _eventShowDatePickerDialog = MutableSharedFlow<Date>()
    val eventShowDatePickerDialog = _eventShowDatePickerDialog.asSharedFlow()

    private val _eventShowDateNotSetToast = MutableSharedFlow<Boolean>()
    val eventShowDateNotSetToast = _eventShowDateNotSetToast.asSharedFlow()

    private val _eventShowTimePickerDialog = MutableSharedFlow<HourAndMinute>()
    val eventShowTimePickerDialog = _eventShowTimePickerDialog.asSharedFlow()

    private val _eventNavigateUp = MutableSharedFlow<Unit>()
    val eventNavigateUp = _eventNavigateUp.asSharedFlow()

    init {
        loadTask()
    }

    private fun loadTask() {
        viewModelScope.launch {
            _taskFlow.value = runCatching {
                val task = taskId?.let { getTaskById(it) } ?: Task()

                _isDatetimeSectionVisible.emit(task.dueDate != null)
                _isChecklistSectionVisible.emit(task.checklist != null)

                CallResult.Success(task)
            }.getOrElse { throwable ->
                CallResult.Error(throwable)
            }
        }
    }

    val isTaskIdGiven: Boolean
        get() = taskId != null

    fun setTitle(text: String) {
        if (task?.title == text) return
        refreshTaskLater { task ->
            task.copy(title = text)
        }
    }

    fun setNote(text: String) {
        if (task?.note == text) return
        refreshTaskLater { task ->
            task.copy(note = text)
        }
    }

    private inline fun refreshTask(
        crossinline newTask: (task: Task) -> Task = { it }
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            val prevTask = task ?: return@launch
            _taskFlow.emit(CallResult.Loading)
            val modified = applyPendingModifications(prevTask)
            val task = newTask(modified)
            _taskFlow.emit(CallResult.Success(task))
        }
    }

    // Used when an immediate update of state is ruining the UX
    // Pending modifications will be applied on the next `refreshTask` call.
    private fun refreshTaskLater(
        refreshTask: (task: Task) -> Task
    ) {
        pendingModifications.add(refreshTask)
    }

    private val pendingModifications: Queue<Function<Task, Task>> = LinkedList()

    private fun applyPendingModifications(task: Task): Task {
        if (pendingModifications.isEmpty()) return task

        val modifyTask = pendingModifications
            .reduce { acc, function ->
                acc.andThen(function)
            }

        pendingModifications.clear()

        return modifyTask.apply(task)
    }

    fun setDatetimeEnabled(enabled: Boolean) {
        viewModelScope.launch {
            _isDatetimeSectionVisible.emit(enabled)
            if (!enabled) {
                clearDatetime()
            }
        }
    }

    private fun clearDatetime() {
        refreshTask { task ->
            task.copy(dueDate = null, isTimeSpecified = false)
        }
    }

    fun setDate(ms: Long) {
        if (task?.isTimeSpecified == true) {
            val hrs = calendar?.get(Calendar.HOUR_OF_DAY) ?: 12
            val min = calendar?.get(Calendar.MINUTE) ?: 0
            refreshTask { task ->
                task.copy(dueDate = Date(ms))
            }
            setTime(hrs, min)
        } else {
            refreshTask { task ->
                task.copy(dueDate = Date(ms))
            }
        }
    }

    fun setTime(hour: Int, minute: Int) {
        refreshTask { task ->
            val newDueDate = task.dueDate?.calendar()
                ?.run {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    time
                }

            task.copy(dueDate = newDueDate, isTimeSpecified = true)
        }
    }

    fun clearTime() {
        refreshTask { task ->
            task.copy(isTimeSpecified = false)
        }
    }

    fun setPeriodicityEnabled(enabled: Boolean) {
        viewModelScope.launch {
            _isPeriodicitySectionVisible.emit(enabled)
        }
    }

    fun setChecklistEnabled(enabled: Boolean) {
        viewModelScope.launch {
            _isChecklistSectionVisible.emit(enabled)
            refreshTask { task ->
                val newChecklist = if (!enabled) {
                    null
                } else {
                    blankChecklist(task)
                }

                task.copy(
                    checklist = newChecklist
                )
            }
        }
    }

    private fun blankChecklist(task: Task) =
        TaskChecklist(
            id = task.id.hashCode().toLong(), taskId = task.id, content = emptyList()
        )

    fun addChecklistEntry(context: Context) {
        refreshTask { task ->
            val oldChecklist = task.checklist
                ?: blankChecklist(task)

            val newChecklist = oldChecklist + ChecklistItem(
                context.getString(
                    R.string.step_placeholder,
                    oldChecklist.content.size + 1
                )
            )

            task.copy(checklist = newChecklist)
        }
    }

    fun onChecklistItemTextChanged(index: Int, text: String) {
        refreshTaskLater { task ->
            val checklist = task.checklist ?: errorNoChecklist()
            val newContent = checklist.content.toMutableList()
            newContent[index] = newContent[index].copy(text = text)
            task.copy(checklist = checklist.copy(content = newContent.toList()))
        }
    }

    fun onChecklistItemChecked(index: Int, isChecked: Boolean) {
        refreshTask { task ->
            val checklist = task.checklist ?: errorNoChecklist()
            val newContent = checklist.content.toMutableList()
            newContent[index] = newContent[index].copy(isDone = isChecked)
            task.copy(checklist = checklist.copy(content = newContent.toList()))
        }
    }

    private fun errorNoChecklist(): Nothing =
        error("Checklist item received an update but no checklist found")

    fun pickDate() {
        viewModelScope.launch {
            _eventShowPickDateMenu.emit(true)
        }
    }

    fun showDatePicker() {
        viewModelScope.launch {
            _eventShowDatePickerDialog.emit(task?.dueDate ?: Date())
        }
    }

    fun pickTime() {
        viewModelScope.launch {
            var hrs = 12
            var min = 0
            val cal = calendar
            if (task?.isTimeSpecified == true && cal != null) {
                hrs = cal.get(Calendar.HOUR_OF_DAY)
                min = cal.get(Calendar.MINUTE)
            }

            _eventShowTimePickerDialog.emit(HourAndMinute(hrs to min))
        }
    }

    fun saveTask() {
        viewModelScope.launch {
            val prevTask = task ?: return@launch
            val task = applyPendingModifications(prevTask)

            if (isTaskIdGiven) {
                updateTask(task)
            } else {
                addTask(task)
            }

            _eventNavigateUp.emit(Unit)
        }
    }

    fun onStop() = refreshTask()
}
