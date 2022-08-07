package io.github.beleavemebe.inbox.common

@JvmInline
value class HourAndMinute(private val pair: Pair<Int, Int> = 12 to 0) {
    val hour get() = pair.first
    val minute get() = pair.second

    fun toMillis(): Long = (hour * HOUR_MS) + (minute * MINUTE_MS)
}
