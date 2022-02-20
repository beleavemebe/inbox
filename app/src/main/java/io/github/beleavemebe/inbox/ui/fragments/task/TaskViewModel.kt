package io.github.beleavemebe.inbox.ui.fragments.task

import androidx.lifecycle.*
import io.github.beleavemebe.inbox.core.model.Task
import io.github.beleavemebe.inbox.core.usecase.AddTask
import io.github.beleavemebe.inbox.core.usecase.GetTaskById
import io.github.beleavemebe.inbox.core.usecase.UpdateTask
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class TaskViewModel @Inject constructor(
    private val getTaskById: GetTaskById,
    private val addTask: AddTask,
    private val updateTask: UpdateTask,
) : ViewModel() {

    val taskId = MutableLiveData<UUID>()

    val task: LiveData<Task> = taskId.switchMap {
        flow {
            val task = if (it != null) getTaskById(it) else Task()
            emit(task)
        }.asLiveData()
    }


    val isTaskIdGiven: Boolean
        get() = taskId.value != null

    private val taskSavingAction: (Task) -> Unit
        get() = if (isTaskIdGiven) ::updateTask else ::addTask

    private fun addTask(task: Task) {
        viewModelScope.launch {
            addTask.invoke(task)
        }
    }

    private fun updateTask(task: Task) {
        viewModelScope.launch {
            updateTask.invoke(task)
        }
    }

    fun saveTask() = taskSavingAction(task.value!!)
}
