package io.github.beleavemebe.inbox.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import io.github.beleavemebe.inbox.tasks.ui.task_details.TaskFragment
import io.github.beleavemebe.inbox.tasks.ui.task_details.di.TaskDetailsDependencies
import io.github.beleavemebe.inbox.tasks.ui.task_list.TaskListFragment
import io.github.beleavemebe.inbox.tasks.ui.task_list.di.TaskListDependencies
import io.github.beleavemebe.inbox.ui.App
import io.github.beleavemebe.inbox.ui.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [
    DataModule::class,
    DataModuleBindings::class,
    DomainModule::class,
    PresentationModule::class,
    TaskListFeatureBindings::class,
    TaskDetailsFeatureBindings::class])
interface AppComponent : TaskListDependencies, TaskDetailsDependencies {
    fun inject(app: App)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
        ): AppComponent
    }
}
