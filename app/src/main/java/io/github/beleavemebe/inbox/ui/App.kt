package io.github.beleavemebe.inbox.ui

import android.app.Application
import io.github.beleavemebe.inbox.di.ServiceLocator

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        ServiceLocator.init(this)
    }
}
