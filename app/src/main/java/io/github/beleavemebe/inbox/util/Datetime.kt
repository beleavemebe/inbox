package io.github.beleavemebe.inbox.util

import android.text.format.DateUtils
import java.util.*

internal val calendar get() = Calendar.getInstance(Locale("ru"))

const val HOUR_MS = 60*60*1000L
const val MINUTE_MS = 60*1000L

internal val Date.isToday get() =
    DateUtils.isToday(this.time)

internal val Date.isYesterday get() =
    Date(this.time + 24 * HOUR_MS).isToday

internal val Date.isTomorrow get() =
    Date(this.time - 24 * HOUR_MS).isToday
