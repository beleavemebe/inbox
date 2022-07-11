package io.github.beleavemebe.inbox.tasks.ui.task_list.di

import dagger.Component
import io.github.beleavemebe.inbox.tasks.ui.task_list.TaskListFragment

@Component(dependencies = [TaskListDependencies::class])
interface TaskListComponent {
    fun inject(fragment: TaskListFragment)

    @Component.Factory
    interface Factory {
        fun create(deps: TaskListDependencies): TaskListComponent
    }
}
