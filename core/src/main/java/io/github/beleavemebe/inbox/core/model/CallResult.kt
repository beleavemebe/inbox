package io.github.beleavemebe.inbox.core.model

sealed class CallResult<out T> {
    object Loading : CallResult<Nothing>()

    data class Success<out T>(val data: T) : CallResult<T>()

    data class Error(val error: CallError? = null) : CallResult<Nothing>()
}

fun <T> CallResult<T>.getOrNull(): T? {
    return if (this is CallResult.Success<T>) data else null
}

open class CallError {
    data class Exception(val t: Throwable) : CallError()
}

fun Throwable.toCallError() = CallError.Exception(this)
