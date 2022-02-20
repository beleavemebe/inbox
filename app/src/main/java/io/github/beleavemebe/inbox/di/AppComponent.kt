package io.github.beleavemebe.inbox.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import io.github.beleavemebe.inbox.ui.fragments.task.TaskFragment
import io.github.beleavemebe.inbox.ui.fragments.tasklist.TaskListFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, DomainModule::class, PresentationModule::class])
interface AppComponent {
    fun inject(fragment: TaskListFragment)
    fun inject(fragment: TaskFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
        ): AppComponent
    }
}
