package io.github.beleavemebe.inbox.tasks.ui.task_details.di

import androidx.lifecycle.Lifecycle
import dagger.BindsInstance
import dagger.Component
import io.github.beleavemebe.inbox.tasks.ui.task_details.TaskFragment

@Component(
    dependencies = [TaskDetailsDependencies::class]
)
interface TaskDetailsComponent {
    fun inject(taskDetailsFragment: TaskFragment)

    @Component.Factory
    interface Factory {
        fun create(
            deps: TaskDetailsDependencies,
            @BindsInstance lifecycle: Lazy<Lifecycle>,
        ): TaskDetailsComponent
    }
}
