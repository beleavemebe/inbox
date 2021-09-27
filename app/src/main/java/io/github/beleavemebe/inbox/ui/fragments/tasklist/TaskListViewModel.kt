package io.github.beleavemebe.inbox.ui.fragments.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.github.beleavemebe.inbox.model.Task
import io.github.beleavemebe.inbox.repositories.TaskRepository

class TaskListViewModel : ViewModel() {
    private val taskRepository = TaskRepository.getInstance()
    val taskListLiveData: LiveData<MutableList<Task>> = taskRepository.getTasks()

    fun deleteTask(index: Int) {
        val task = taskListLiveData.value?.removeAt(index)
        taskRepository.deleteTask(task ?: return)
    }

    fun insertTask(task: Task, position: Int) {
        taskListLiveData.value?.add(position, task)
        taskRepository.addTask(task)
    }
}
