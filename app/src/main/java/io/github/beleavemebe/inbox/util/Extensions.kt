package io.github.beleavemebe.inbox.util

import android.graphics.Paint
import android.widget.TextView
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import io.github.beleavemebe.inbox.ui.activities.MainActivity

@MainThread internal fun Fragment.hideBottomNavMenu() {
    (requireActivity() as MainActivity)
        .hideBottomNavMenu()
}

@MainThread internal fun Fragment.revealBottomNavMenu() {
    (requireActivity() as MainActivity)
        .revealBottomNavMenu()
}

internal fun TextView.setCrossedOut(crossedOut: Boolean) {
    if (crossedOut) crossOut() else uncross()
}

private fun TextView.crossOut() {
    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

private fun TextView.uncross() {
    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
}
