package io.github.beleavemebe.inbox.di

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import io.github.beleavemebe.inbox.core.di.Dependencies
import io.github.beleavemebe.inbox.core.di_dagger.DependenciesKey
import io.github.beleavemebe.inbox.tasks.ui.task_list.di.TaskListDependencies

@Module
interface TaskListFeatureBindings {
    @Binds
    @IntoMap
    @DependenciesKey(TaskListDependencies::class)
    fun bindTaskListDependencies(impl: AppComponent): Dependencies
}
