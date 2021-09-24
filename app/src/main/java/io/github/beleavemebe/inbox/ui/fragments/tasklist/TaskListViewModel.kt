package io.github.beleavemebe.inbox.ui.fragments.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.github.beleavemebe.inbox.model.Task
import io.github.beleavemebe.inbox.repositories.TaskRepository

class TaskListViewModel : ViewModel() {
    private val taskRepository = TaskRepository.getInstance()
    val taskListLiveData: LiveData<List<Task>> = taskRepository.getTasks()
}
