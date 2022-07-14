package io.github.beleavemebe.inbox.di

import dagger.Module
import dagger.Provides
import io.github.beleavemebe.inbox.tasks.data.di.TasksDataApi
import io.github.beleavemebe.inbox.tasks.domain.repository.ChecklistRepository
import io.github.beleavemebe.inbox.tasks.domain.repository.TaskRepository

@Module
object TasksDataModule {
    @Provides
    @AppScope
    fun provideTasksDataApi(
        tasksDataDependencies: AppComponent
    ): TasksDataApi {
        return TasksDataApi.create(tasksDataDependencies)
    }

    @Provides
    @AppScope
    fun provideTaskRepository(
        tasksDataApi: TasksDataApi
    ): TaskRepository {
        return tasksDataApi.taskRepository
    }

    @Provides
    @AppScope
    fun provideChecklistRepository(
        tasksDataApi: TasksDataApi
    ): ChecklistRepository {
        return tasksDataApi.checklistRepository
    }
}
