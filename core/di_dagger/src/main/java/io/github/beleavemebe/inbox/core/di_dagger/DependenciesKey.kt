package io.github.beleavemebe.inbox.core.di_dagger

import dagger.MapKey
import io.github.beleavemebe.inbox.core.di.Dependencies
import kotlin.reflect.KClass

@MapKey
annotation class DependenciesKey(val value: KClass<out Dependencies>)
