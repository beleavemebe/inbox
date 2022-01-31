package io.github.beleavemebe.inbox.ui.fragments.task

import android.text.format.DateFormat
import androidx.lifecycle.*
import io.github.beleavemebe.inbox.core.model.Task
import io.github.beleavemebe.inbox.core.usecase.AddTask
import io.github.beleavemebe.inbox.core.usecase.GetTaskById
import io.github.beleavemebe.inbox.core.usecase.UpdateTask
import io.github.beleavemebe.inbox.di.ServiceLocator
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.*

class TaskViewModel(
    private val taskId: UUID?,
    private val getTaskById: GetTaskById,
    private val addTask: AddTask,
    private val updateTask: UpdateTask,
) : ViewModel() {

    val task: LiveData<Task> =
        flow {
            val task = if (taskId != null) getTaskById(taskId) else Task()
            emit(task)
        }
            .asLiveData()

    val isTaskIdGiven: Boolean
        get() = taskId != null

    private val taskSavingAction: (Task) -> Unit
        get() = if (isTaskIdGiven) ::updateTask else ::addTask

    private fun addTask(task: Task) =
        viewModelScope.launch {
            addTask.invoke(task)
        }

    private fun updateTask(task: Task) =
        viewModelScope.launch {
            updateTask.invoke(task)
        }

    fun saveTask() = taskSavingAction(task.value!!)

    fun getFormattedDate(date: Date) =
        DateFormat.format("EEE, d MMM yyyy", date).toString()
            .replaceFirstChar(Char::uppercase)

    fun getFormattedTimestamp(date: Date) =
        DateFormat.format("dd MMM `yy HH:mm", date).toString()

    companion object {
        fun provideFactory(
            taskId: UUID?,
        ) = object : ViewModelProvider.Factory {
            @Suppress("unchecked_cast")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TaskViewModel(
                    taskId,
                    ServiceLocator.getTaskById,
                    ServiceLocator.addTask,
                    ServiceLocator.updateTask,
                ) as T
            }
        }
    }

}
