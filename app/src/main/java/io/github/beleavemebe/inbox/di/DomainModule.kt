package io.github.beleavemebe.inbox.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.github.beleavemebe.inbox.core.repository.TaskRepository
import io.github.beleavemebe.inbox.core.usecase.*

@Module
class DomainModule {
    @Provides
    @Reusable
    fun provideGetTaskById(
        taskRepository: TaskRepository,
    ): GetTaskById {
        return GetTaskById(taskRepository)
    }

    @Provides
    @Reusable
    fun provideGetTasksInteractor(
        taskRepository:TaskRepository,
    ): GetTasksInteractor {
        return GetTasksInteractor(taskRepository)
    }

    @Provides
    @Reusable
    fun provideAddTask(
        taskRepository: TaskRepository,
    ): AddTask {
        return AddTask(taskRepository)
    }

    @Provides
    @Reusable
    fun provideUpdateTask(
        taskRepository: TaskRepository,
    ): UpdateTask {
        return UpdateTask(taskRepository)
    }

    @Provides
    @Reusable
    fun provideDeleteTask(
        taskRepository: TaskRepository,
    ): DeleteTask {
        return DeleteTask(taskRepository)
    }
}
