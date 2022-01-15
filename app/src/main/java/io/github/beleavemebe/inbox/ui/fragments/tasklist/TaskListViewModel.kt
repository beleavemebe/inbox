package io.github.beleavemebe.inbox.ui.fragments.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.beleavemebe.inbox.model.Task
import io.github.beleavemebe.inbox.repositories.TaskRepository
import kotlinx.coroutines.launch

class TaskListViewModel : ViewModel() {
    private val taskRepository = TaskRepository.getInstance()
    val tasks: LiveData<List<Task>> = taskRepository.getTasks()

    fun deleteTask(index: Int) {
        val task = tasks.value?.get(index)
        viewModelScope.launch {
            taskRepository.deleteTask(task ?: return@launch)
        }
    }

    fun insertTask(task: Task) {
        viewModelScope.launch {
            taskRepository.addTask(task)
        }
    }
}
