package io.github.beleavemebe.inbox.ui.task

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

    fun saveTask() {
        val task = task.value ?: return
        viewModelScope.launch {
            if (isTaskIdGiven) {
                updateTask(task)
            } else {
                addTask(task)
            }
        }
    }
}
