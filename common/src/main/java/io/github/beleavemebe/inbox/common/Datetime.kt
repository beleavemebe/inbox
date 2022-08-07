package io.github.beleavemebe.inbox.common

import java.util.*

fun Date.calendar(): Calendar {
    val date = this
    return Calendar.getInstance(
        Locale.getDefault()
    ).apply {
        time = date
    }
}

const val MINUTE_MS = 60 * 1000L
const val HOUR_MS = 60 * MINUTE_MS
const val DAY_MS = 24 * HOUR_MS
const val WEEK_MS = 7 * DAY_MS

val lastMonday: Long
    get() = Date().calendar().run {
        set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        setStartOfTheDay()
        if (time.isFuture) {
            timeInMillis -= WEEK_MS
        }
        timeInMillis
    }

val weekEnd: Long
    get() = Date().calendar().run {
        set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        setEndOfTheDay()
        if (time.isPast) {
            timeInMillis += WEEK_MS
        }
        timeInMillis
    }

val nextWeekEnd: Long
    get() = weekEnd + WEEK_MS

val Date.isPast: Boolean
    get() = time < System.currentTimeMillis()

val Date.isFuture: Boolean
    get() = !isPast

val Date.dayOfWeek: Int
    get() = calendar().get(Calendar.DAY_OF_WEEK)

fun Calendar.setEndOfTheDay() {
    set(Calendar.HOUR_OF_DAY, 23)
    set(Calendar.MINUTE, 59)
    set(Calendar.SECOND, 59)
    set(Calendar.MILLISECOND, 999)
}

fun Calendar.setStartOfTheDay() {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}

