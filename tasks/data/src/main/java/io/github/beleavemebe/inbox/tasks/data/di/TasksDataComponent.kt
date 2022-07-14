package io.github.beleavemebe.inbox.tasks.data.di

import dagger.Component

@Component(
    dependencies = [TasksDataDependencies::class],
    modules = [
        DataModule::class,
        DataModuleBindings::class
    ]
)
interface TasksDataComponent : TasksDataApi {
    @Component.Factory
    interface Factory {
        fun create(deps: TasksDataDependencies): TasksDataComponent
    }
}
