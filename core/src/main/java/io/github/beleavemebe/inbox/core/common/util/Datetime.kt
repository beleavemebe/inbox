package io.github.beleavemebe.inbox.core.common.util

import java.util.*

fun Date.calendar(): Calendar {
    val date = this
    return Calendar.getInstance(Locale.getDefault()).apply {
        time = date
    }
}

const val MINUTE_MS = 60 * 1000L
const val HOUR_MS = 60 * MINUTE_MS
const val DAY_MS = 24 * HOUR_MS

val todayMs: Long
    get() = Date().calendar().run {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        timeInMillis
    }

val weekEndMs: Long
    get() = Date().calendar().run {
        val current = time.time
        set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        if (timeInMillis < current) {
            timeInMillis += 7 * DAY_MS
        }
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
        timeInMillis
    }

val nextWeekEndMs: Long
    get() = weekEndMs + (7L * 24L * HOUR_MS)
