package io.github.beleavemebe.inbox.ui.tasklist

import androidx.lifecycle.*
import io.github.beleavemebe.inbox.core.model.Task
import io.github.beleavemebe.inbox.core.usecase.*
import io.github.beleavemebe.inbox.ui.util.log
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class TaskListViewModel @Inject constructor(
    private val getTasksInteractor: GetTasksInteractor,
    private val getTaskById: GetTaskById,
    private val deleteTask: DeleteTask,
    private val addTask: AddTask,
    private val updateTask: UpdateTask,
) : ViewModel() {

    private val _taskFilterPreference = MutableStateFlow(TaskFilterPreference.UNFILTERED)
    val taskFilterPreference: StateFlow<TaskFilterPreference> = _taskFilterPreference

    val tasks: LiveData<List<Task>> =
        _taskFilterPreference.flatMapLatest {
            log(it)
            when (it) {
                TaskFilterPreference.UNFILTERED -> {
                    getTasksInteractor.getTasks()
                }

                TaskFilterPreference.UNDATED -> {
                    getTasksInteractor.getUndatedTasks()
                }

                TaskFilterPreference.DUE_THIS_WEEK -> {
                    getTasksInteractor.getTasksDueThisWeek()
                }

                TaskFilterPreference.DUE_THIS_OR_NEXT_WEEK -> {
                    getTasksInteractor.getTasksDueThisOrNextWeek()
                }
            }
        }
            .asLiveData()

    val taskFilterPreferenceLiveData: LiveData<TaskFilterPreference>
        get() = _taskFilterPreference.asLiveData()

    fun onPreferenceSelected(preference: TaskFilterPreference) {
        _taskFilterPreference.value = preference
    }

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
}