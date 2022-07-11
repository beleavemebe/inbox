package io.github.beleavemebe.inbox.tasks.ui.task_list.di

import androidx.lifecycle.ViewModelProvider
import io.github.beleavemebe.inbox.core.di.Dependencies
import io.github.beleavemebe.inbox.tasks.domain.usecase.AddTask
import io.github.beleavemebe.inbox.tasks.domain.usecase.DeleteTask
import io.github.beleavemebe.inbox.tasks.domain.usecase.GetTaskById
import io.github.beleavemebe.inbox.tasks.domain.usecase.GetTasks
import io.github.beleavemebe.inbox.tasks.domain.usecase.UpdateTask

interface TaskListDependencies : Dependencies {
    val vmFactory: ViewModelProvider.Factory
    val getTasks: GetTasks
    val getTaskById: GetTaskById
    val deleteTask: DeleteTask
    val addTask: AddTask
    val updateTask: UpdateTask
}
