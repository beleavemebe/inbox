package io.github.beleavemebe.inbox.ui.fragments.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.github.beleavemebe.inbox.model.Task
import io.github.beleavemebe.inbox.repositories.TaskRepository
import java.text.SimpleDateFormat
import java.util.*

class TaskViewModel : ViewModel() {
    private val taskRepository = TaskRepository.getInstance()
    private val taskIdMutableLiveData = MutableLiveData<UUID>()

    var taskLiveData : LiveData<Task?> =
        Transformations.switchMap(taskIdMutableLiveData) { id ->
            taskRepository.getTask(id)
        }

    // Add or update task depending on whether or not the task id is passed as an argument
    var isTaskIdGiven: Boolean = false
    private val taskHandleAction : (Task) -> Unit
        get() = if (isTaskIdGiven) ::updateTask else ::addTask

    fun onNoTaskIdGiven() {
        isTaskIdGiven = false
    }

    fun onTaskIdGiven(taskId: UUID) {
        isTaskIdGiven = true
        taskIdMutableLiveData.value = taskId
    }

    fun onExitFragment(task: Task) = taskHandleAction(task)

    fun getFormattedDate(d : Date) =
        SimpleDateFormat("EEE, dd MMM yyyy", Locale("ru"))
            .format(d)
            .replaceFirstChar { it.uppercase() }

    fun getFormattedTimestamp(d : Date) =
        SimpleDateFormat("dd MMM `yy HH:mm", Locale("ru"))
            .format(d) as String

    private fun addTask(task: Task) {
        taskRepository.addTask(task)
    }

    private fun updateTask(task: Task) {
        taskRepository.updateTask(task)
    }
}