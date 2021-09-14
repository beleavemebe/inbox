package io.github.beleavemebe.inbox.util

import android.text.format.DateUtils
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import io.github.beleavemebe.inbox.ui.activities.MainActivity
import java.util.*

@MainThread internal fun Fragment.hideBottomNavMenu() {
    (requireActivity() as MainActivity)
        .hideBottomNavMenu()
}

@MainThread internal fun Fragment.revealBottomNavMenu() {
    (requireActivity() as MainActivity)
        .revealBottomNavMenu()
}

internal val Date.isToday get() =
    DateUtils.isToday(this.time)

internal val Date.isYesterday get() =
    Date(this.time + 24 * HOUR_MS).isToday

internal val Date.isTomorrow get() =
    Date(this.time - 24 * HOUR_MS).isToday