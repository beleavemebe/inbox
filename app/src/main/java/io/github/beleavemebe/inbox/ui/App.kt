package io.github.beleavemebe.inbox.ui

import android.app.Application
import android.content.Context
import io.github.beleavemebe.inbox.di.AppComponent
import io.github.beleavemebe.inbox.di.DaggerAppComponent

class App : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(context = this)
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> applicationContext.appComponent
    }
