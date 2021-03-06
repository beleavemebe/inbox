package io.github.beleavemebe.inbox.tasks.data.di

import dagger.Binds
import dagger.Module
import io.github.beleavemebe.inbox.tasks.data.repository.TaskRepositoryImpl
import io.github.beleavemebe.inbox.tasks.domain.repository.ChecklistRepository
import io.github.beleavemebe.inbox.tasks.domain.repository.TaskRepository

@Module
interface DataModuleBindings {
    @Binds
    fun bindTaskRepository(
        impl: TaskRepositoryImpl
    ): TaskRepository

    @Binds
    fun bindChecklistRepository(
        impl: TaskRepositoryImpl
    ): ChecklistRepository
}
