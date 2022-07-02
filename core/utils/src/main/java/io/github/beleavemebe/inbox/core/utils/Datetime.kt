package io.github.beleavemebe.inbox.core.utils

import android.text.format.DateUtils
import io.github.beleavemebe.inbox.domain.common.util.DAY_MS
import java.util.*

val Date?.isToday get() =
    this?.let { DateUtils.isToday(it.time) } ?: false

val Date?.isYesterday get() =
    this?.let { Date(it.time + DAY_MS).isToday } ?: false

val Date?.isTomorrow get() =
    this?.let { Date(it.time - DAY_MS).isToday } ?: false
