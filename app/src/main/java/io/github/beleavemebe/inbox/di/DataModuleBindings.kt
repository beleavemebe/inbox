package io.github.beleavemebe.inbox.di

import dagger.Binds
import dagger.Module
import io.github.beleavemebe.inbox.domain.repository.ChecklistRepository
import io.github.beleavemebe.inbox.domain.repository.TaskRepository
import io.github.beleavemebe.inbox.data.repository.TaskRepositoryImpl

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