package io.github.beleavemebe.inbox.di

import dagger.BindsInstance
import dagger.Component
import io.github.beleavemebe.inbox.ui.MainActivity

@Component(
    modules = [NavigationModule::class]
)
interface MainActivityComponent {
    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance mainActivity: MainActivity
        ): MainActivityComponent
    }
}
