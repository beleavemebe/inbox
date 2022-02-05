package io.github.beleavemebe.inbox.ui.util

import android.text.format.DateUtils
import io.github.beleavemebe.inbox.core.common.util.DAY_MS
import java.util.*

internal val Date?.isToday get() =
    this?.let { DateUtils.isToday(it.time) } ?: false

internal val Date?.isYesterday get() =
    this?.let { Date(it.time + DAY_MS).isToday } ?: false

internal val Date?.isTomorrow get() =
    this?.let { Date(it.time - DAY_MS).isToday } ?: false
