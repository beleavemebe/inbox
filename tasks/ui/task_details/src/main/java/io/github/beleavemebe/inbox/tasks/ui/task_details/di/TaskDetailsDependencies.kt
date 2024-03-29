package io.github.beleavemebe.inbox.tasks.ui.task_details.di

import android.content.Context
import io.github.beleavemebe.inbox.core.di.Dependencies
import io.github.beleavemebe.inbox.tasks.domain.usecase.AddTask
import io.github.beleavemebe.inbox.tasks.domain.usecase.GetTaskById
import io.github.beleavemebe.inbox.tasks.domain.usecase.UpdateTask

interface TaskDetailsDependencies : Dependencies {
    val context: Context
    val getTaskById: GetTaskById
    val addTask: AddTask
    val updateTask: UpdateTask
}
