package io.github.beleavemebe.inbox.di

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import io.github.beleavemebe.inbox.core.di.Dependencies
import io.github.beleavemebe.inbox.core.di_dagger.DependenciesKey
import io.github.beleavemebe.inbox.tasks.ui.task_details.di.TaskDetailsDependencies

@Module
interface TaskDetailsFeatureBindings {
    @Binds
    @IntoMap
    @DependenciesKey(TaskDetailsDependencies::class)
    fun bindTaskListDependencies(impl: AppComponent): Dependencies
}
