package io.github.beleavemebe.inbox.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import io.github.beleavemebe.inbox.tasks.data.di.TasksDataDependencies
import io.github.beleavemebe.inbox.tasks.ui.task_details.di.TaskDetailsDependencies
import io.github.beleavemebe.inbox.tasks.ui.task_list.di.TaskListDependencies
import io.github.beleavemebe.inbox.ui.App

@AppScope
@Component(
    modules = [
        DomainModule::class,
        MoshiModule::class,
        TasksDataModule::class,
        TaskListFeatureBindings::class,
        TaskDetailsFeatureBindings::class,
        ViewModelBindings::class,
    ]
)
interface AppComponent : TaskListDependencies, TaskDetailsDependencies, TasksDataDependencies {
    fun inject(app: App)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
        ): AppComponent
    }
}
