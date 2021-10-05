package io.github.beleavemebe.inbox

import android.app.Application
import io.github.beleavemebe.inbox.repositories.TaskRepository

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeRepositories()
    }

    private fun initializeRepositories() {
        TaskRepository.initialize(this)
    }
}
