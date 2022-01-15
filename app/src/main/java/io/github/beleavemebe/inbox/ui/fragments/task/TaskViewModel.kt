package io.github.beleavemebe.inbox.ui.fragments.task

import android.text.format.DateFormat
import androidx.lifecycle.*
import io.github.beleavemebe.inbox.model.Task
import io.github.beleavemebe.inbox.repositories.TaskRepository
import kotlinx.coroutines.launch
import java.lang.UnsupportedOperationException
import java.util.*

class TaskViewModel : ViewModel() {
    private val taskIdMutableLiveData = MutableLiveData<UUID?>()

    val task: LiveData<Task> =
        taskIdMutableLiveData.switchMap { id: UUID? ->
            if (id != null) {
                taskRepository.getTask(id)
            } else {
                MutableLiveData(Task())
            }
        }

    var isTaskIdGiven: Boolean = false
        private set

    var taskId: UUID?
        get() = throw UnsupportedOperationException()
        set(value) {
            isTaskIdGiven = value != null
            taskIdMutableLiveData.value = value
        }

    fun saveTask() = taskSavingAction(task.value!!)

    private val taskSavingAction: (Task) -> Unit
        get() = if (isTaskIdGiven) ::updateTask else ::addTask

    fun getFormattedDate(date: Date) =
        DateFormat.format("EEE, d MMM yyyy", date).toString()
            .replaceFirstChar { it.uppercase() }

    fun getFormattedTimestamp(date : Date) =
        DateFormat.format("dd MMM `yy HH:mm", date).toString()

    private val taskRepository = TaskRepository.getInstance()

    private fun addTask(task: Task) =
        viewModelScope.launch {
            taskRepository.addTask(task)
        }

    private fun updateTask(task: Task) =
        viewModelScope.launch {
            taskRepository.updateTask(task)
        }
}
