package io.github.beleavemebe.inbox.ui

import android.app.Application
import io.github.beleavemebe.inbox.core.di.DepsMap
import io.github.beleavemebe.inbox.core.di.HasDependencies
import io.github.beleavemebe.inbox.di.DaggerAppComponent
import javax.inject.Inject

class App : Application(), HasDependencies {
    @Inject override lateinit var depsMap: DepsMap

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.factory()
            .create(context = this)
            .inject(this)
    }
}
