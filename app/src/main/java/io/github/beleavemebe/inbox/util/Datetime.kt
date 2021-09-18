package io.github.beleavemebe.inbox.util

import android.text.format.DateUtils
import java.util.*

internal val calendar get() = Calendar.getInstance(Locale("ru"))

const val HOUR_MS = 60*60*1000L
const val MINUTE_MS = 60*1000L

internal val Date?.isToday get() =
    this?.let { DateUtils.isToday(it.time) } ?: false

internal val Date?.isYesterday get() =
    this?.let { Date(it.time + 24 * HOUR_MS).isToday } ?: false

internal val Date?.isTomorrow get() =
    this?.let { Date(it.time - 24 * HOUR_MS).isToday } ?: false

internal val Date?.isPast get() =
    this?. let { Date() > it } ?: false
