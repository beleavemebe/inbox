package io.github.beleavemebe.inbox.di

import android.app.Application
import io.github.beleavemebe.inbox.core.repository.TaskRepository
import io.github.beleavemebe.inbox.core.usecase.*
import io.github.beleavemebe.inbox.data.repository.TaskRepositoryImpl

object ServiceLocator {
    lateinit var application: Application

    private val taskRepository: TaskRepository by lazy {
        TaskRepositoryImpl(application)
    }

    val getTaskById get() = GetTaskById(taskRepository)
    val getTasksDueThisWeek get() = GetTasksDueThisWeek(taskRepository)
    val getTasksDueThisOrNextWeek get() = GetTasksDueThisOrNextWeek(taskRepository)
    val getTasks get() = GetTasks(taskRepository)
    val addTask get() = AddTask(taskRepository)
    val updateTask get() = UpdateTask(taskRepository)
    val deleteTask get() = DeleteTask(taskRepository)

    fun init(application: Application) {
        this.application = application
    }
}
