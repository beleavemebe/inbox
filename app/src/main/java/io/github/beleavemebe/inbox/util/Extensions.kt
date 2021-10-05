package io.github.beleavemebe.inbox.util

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import io.github.beleavemebe.inbox.ui.activities.MainActivity

internal val Fragment.bottomNavView get() =
    (requireActivity() as MainActivity)
        .getBottomNavigationView()

@MainThread internal fun Fragment.hideBottomNavMenu() {
    (requireActivity() as MainActivity)
        .hideBottomNavMenu()
}

@MainThread internal fun Fragment.revealBottomNavMenu() {
    (requireActivity() as MainActivity)
        .revealBottomNavMenu()
}

internal fun Context?.toast(@StringRes stringRes: Int) {
    Toast.makeText(
        this ?: return,
        stringRes,
        Toast.LENGTH_SHORT
    ).show()
}

internal fun log(msg: String) = Log.d("app-debug", msg)

internal fun TextView.setCrossedOut(crossedOut: Boolean) {
    if (crossedOut) crossOut() else uncross()
}

private fun TextView.crossOut() {
    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

private fun TextView.uncross() {
    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
}
