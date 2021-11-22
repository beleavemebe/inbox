package io.github.beleavemebe.inbox.ui.fragments.task

import android.text.format.DateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.github.beleavemebe.inbox.model.Task
import io.github.beleavemebe.inbox.repositories.TaskRepository
import java.util.*

class TaskViewModel : ViewModel() {
    private val taskRepository = TaskRepository.getInstance()
    private val taskIdMutableLiveData = MutableLiveData<UUID>()

    var taskLiveData: LiveData<Task?> =
        Transformations.switchMap(taskIdMutableLiveData) { id ->
            taskRepository.getTask(id)
        }

    var isTaskIdGiven: Boolean = false

    private val taskHandleAction: (Task) -> Unit
        get() = if (isTaskIdGiven) ::updateTask else ::addTask

    var taskId: UUID? = null
        set(value) =
            value?.let {
                taskIdMutableLiveData.value = it
                isTaskIdGiven = true
            } ?: run {
                isTaskIdGiven = false
            }

    fun handleTask(task: Task) = taskHandleAction(task)

    fun getFormattedDate(date: Date) =
        DateFormat.format("EEE, d MMM yyyy", date).toString()
            .replaceFirstChar { it.uppercase() }

    fun getFormattedTimestamp(date : Date) =
        DateFormat.format("dd MMM `yy HH:mm", date).toString()

    private fun addTask(task: Task) {
        taskRepository.addTask(task)
    }

    private fun updateTask(task: Task) {
        taskRepository.updateTask(task)
    }
}
