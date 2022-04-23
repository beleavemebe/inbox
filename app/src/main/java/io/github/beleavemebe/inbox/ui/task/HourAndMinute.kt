package io.github.beleavemebe.inbox.ui.task

@JvmInline
value class HourAndMinute(private val pair: Pair<Int, Int> = 12 to 0) {
    val hour get() = pair.first
    val minute get() = pair.second
}
