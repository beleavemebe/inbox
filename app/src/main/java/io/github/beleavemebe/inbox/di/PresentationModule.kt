package io.github.beleavemebe.inbox.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import io.github.beleavemebe.inbox.tasks.ui.task_list.TaskListViewModel

@Module
abstract class PresentationModule {
    @Binds
    abstract fun bindViewModelFactory(factory: MultiViewModelFactory): ViewModelProvider.Factory

    @Binds
    @[IntoMap ViewModelKey(TaskListViewModel::class)]
    abstract fun bindTaskListViewModel(viewModel: TaskListViewModel): ViewModel
}
