package io.github.beleavemebe.inbox.ui.util

import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

fun <T> Flow<T>.launchWhenStarted(scope: LifecycleCoroutineScope) {
    val flow = this
    scope.launchWhenStarted {
        flow.collect()
    }
}
