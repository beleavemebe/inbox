package io.github.beleavemebe.inbox.di

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class ViewModelKey(
    val value: KClass<out ViewModel>
)
