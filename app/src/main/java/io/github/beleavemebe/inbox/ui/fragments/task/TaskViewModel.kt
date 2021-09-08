package io.github.beleavemebe.inbox.ui.fragments.task

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

    var taskLiveData : LiveData<Task?> =
        Transformations.switchMap(taskIdMutableLiveData) { id ->
            taskRepository.getTask(id)
        }

    // Add or update task depending on whether or not the task id is passed as an argument
    private lateinit var taskHandleAction : (Task) -> Unit
    fun onExitFragment(task: Task) = taskHandleAction(task)

    fun onNoTaskIdGiven() {
        taskHandleAction = ::addTask
    }

    fun loadTask(taskId: UUID) {
        taskIdMutableLiveData.value = taskId
        taskHandleAction = ::updateTask
    }

    private fun addTask(task: Task) {
        taskRepository.addTask(task)
    }

    private fun updateTask(task: Task) {
        taskRepository.updateTask(task)
    }
}