package io.github.beleavemebe.inbox.ui.fragments.tasklist

import androidx.lifecycle.*
import io.github.beleavemebe.inbox.core.model.Task
import io.github.beleavemebe.inbox.core.usecase.*
import io.github.beleavemebe.inbox.di.ServiceLocator
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class TaskListViewModel(
    private val getTasks: GetTasks,
    private val getUndatedTasks: GetUndatedTasks,
    private val getTasksDueThisWeek: GetTasksDueThisWeek,
    private val getTasksDueThisOrNextWeek: GetTasksDueThisOrNextWeek,
    private val getTaskById: GetTaskById,
    private val deleteTask: DeleteTask,
    private val addTask: AddTask,
    private val updateTask: UpdateTask,
) : ViewModel() {

    val taskFilterPreference = MutableStateFlow(TaskFilterPreference.UNFILTERED)

    val tasks: LiveData<List<Task>> =
        taskFilterPreference.flatMapLatest {
            when (it) {
                TaskFilterPreference.UNFILTERED -> getTasks()
                TaskFilterPreference.UNDATED -> getUndatedTasks()
                TaskFilterPreference.DUE_THIS_WEEK -> getTasksDueThisWeek()
                TaskFilterPreference.DUE_THIS_OR_NEXT_WEEK -> getTasksDueThisOrNextWeek()
            }
        }
            .asLiveData()

    val taskFilterPreferenceLiveData: LiveData<TaskFilterPreference>
        get() = taskFilterPreference.asLiveData()

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            deleteTask.invoke(task)
        }
    }

    fun insertTask(task: Task) {
        viewModelScope.launch {
            addTask.invoke(task)
        }
    }

    fun setTaskCompleted(id: UUID, flag: Boolean) {
        viewModelScope.launch {
            val task = getTaskById(id)
            task.isCompleted = flag
            updateTask(task)
        }
    }

    companion object {
        fun provideFactory() = object : ViewModelProvider.Factory {
            @Suppress("unchecked_cast")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TaskListViewModel(
                    ServiceLocator.getTasks,
                    ServiceLocator.getUndatedTasks,
                    ServiceLocator.getTasksDueThisWeek,
                    ServiceLocator.getTasksDueThisOrNextWeek,
                    ServiceLocator.getTaskById,
                    ServiceLocator.deleteTask,
                    ServiceLocator.addTask,
                    ServiceLocator.updateTask,
                ) as T
            }
        }
    }
}
