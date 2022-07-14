package io.github.beleavemebe.inbox.tasks.data.di

import android.content.Context
import com.squareup.moshi.Moshi

interface TasksDataDependencies {
    val context: Context
    val moshi: Moshi
}
