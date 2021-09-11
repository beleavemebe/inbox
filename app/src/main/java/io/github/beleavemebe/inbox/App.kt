package io.github.beleavemebe.inbox

import android.app.Application
import io.github.beleavemebe.inbox.repositories.TaskRepository
import io.github.beleavemebe.inbox.util.Toaster

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        TaskRepository.initialize(this)
        Toaster.initialize(this)
    }
}