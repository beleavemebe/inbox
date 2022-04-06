package io.github.beleavemebe.inbox.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.github.beleavemebe.inbox.core.common.util.calendar
import io.github.beleavemebe.inbox.core.model.*
import io.github.beleavemebe.inbox.core.usecase.AddTask
import io.github.beleavemebe.inbox.core.usecase.GetTaskById
import io.github.beleavemebe.inbox.core.usecase.UpdateTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import java.util.function.Function

class TaskViewModel @AssistedInject constructor(
    @Assisted private val taskId: UUID? = null,
    private val getTaskById: GetTaskById,
    private val addTask: AddTask,
    private val updateTask: UpdateTask,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(taskId: UUID?): TaskViewModel
    }

    private val _taskFlow = MutableStateFlow<CallResult<Task>>(CallResult.Loading)
    val taskFlow = _taskFlow.asStateFlow()

    val task: Task?
        get() = taskFlow.value.getOrNull()

    val calendar: Calendar?
        get() = task?.dueDate?.calendar()

    init {
        loadTask()
    }

    private fun loadTask() {
        viewModelScope.launch {
            _taskFlow.value = runCatching {
                val task = taskId?.let { getTaskById(it) } ?: Task()
                CallResult.Success(task)
            }.getOrElse { throwable ->
                CallResult.Error(throwable.toCallError())
            }
        }
    }

    val isTaskIdGiven: Boolean
        get() = taskId != null

    fun saveTask() {
        viewModelScope.launch {
            val prevTask = task ?: return@launch
            val task = applyPendingModifications(prevTask)

            if (isTaskIdGiven) {
                updateTask(task)
            } else {
                addTask(task)
            }
        }
    }

    fun setTitle(text: String) {
        if (task?.title == text) return
        refreshTaskLater {
            it.copy(title = text)
        }
    }

    fun setNote(text: String) {
        if (task?.note == text) return
        refreshTaskLater {
            it.copy(note = text)
        }
    }

    fun setDate(ms: Long) {
        refreshTask { it.copy(dueDate = Date(ms)) }

        if (task?.isTimeSpecified == true) {
            val hrs = calendar?.get(Calendar.HOUR_OF_DAY) ?: 12
            val min = calendar?.get(Calendar.MINUTE) ?: 0
            setTime(hrs, min)
        }
    }

    fun setTime(hour: Int, minute: Int) {
        val task = task ?: return
        val newDueDate = task.dueDate?.calendar()
            ?.run {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                time
            }

        refreshTask {
            task.copy(dueDate = newDueDate, isTimeSpecified = true)
        }
    }

    fun clearDatetime() {
        clearDate()
        clearTime()
    }

    private fun clearDate() {
        refreshTask { it.copy(dueDate = null) }
    }

    fun clearTime() = setTime(0, 0)

    fun addChecklistEntry() {
        refreshTask { task ->
            val oldChecklist = task.checklist
                ?: TaskChecklist(
                    task.id.hashCode().toLong(),
                    task.id,
                    emptyList()
                )

            val newChecklist = oldChecklist + ChecklistItem(text = "Checklist step")

            task.copy(checklist = newChecklist)
        }
    }

    fun onChecklistItemTextChanged(index: Int, text: String) {
        refreshTaskLater {
            val checklist = it.checklist!!
            val newContent = checklist.content.toMutableList()
            newContent[index] = newContent[index].copy(text = text)
            it.copy(checklist = checklist.copy(content = newContent.toList()))
        }
    }

    fun onChecklistItemChecked(index: Int, isChecked: Boolean) {
        refreshTask {
            val checklist = it.checklist!!
            val newContent = checklist.content.toMutableList()
            newContent[index] = newContent[index].copy(isDone = isChecked)
            it.copy(checklist = checklist.copy(content = newContent.toList()))
        }
    }

    private inline fun refreshTask(
        crossinline newTask: (Task) -> Task
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
    private fun refreshTaskLater(
        refreshTask: (Task) -> Task
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
}
