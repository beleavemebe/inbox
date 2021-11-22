package io.github.beleavemebe.inbox.util

import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import io.github.beleavemebe.inbox.ui.activities.MainActivity

internal fun log(msg: String) = Log.d("app-debug", msg)

internal val Fragment.mainBottomNavigationView get() =
    (requireActivity() as MainActivity)
        .bottomNavigationView

internal fun Fragment.hideBottomNavMenu() {
    (requireActivity() as MainActivity)
        .hideBottomNavMenu()
}

internal fun Fragment.revealBottomNavMenu() {
    (requireActivity() as MainActivity)
        .revealBottomNavMenu()
}

internal val Fragment.mainToolbar get() =
    (requireActivity() as MainActivity)
        .mainToolbar

internal fun Fragment.drawable(@DrawableRes drawableResId: Int) : Drawable? =
    AppCompatResources.getDrawable(requireContext(), drawableResId)

internal inline fun <reified T> Fragment.getTypedArg(key: String?): T? =
    arguments?.get(key) as? T

internal fun Context?.toast(@StringRes stringRes: Int) {
    if (this == null) return
    toast(getString(stringRes))
}

internal fun Context?.toast(text: String) {
    Toast.makeText(
        this ?: return,
        text,
        Toast.LENGTH_SHORT
    ).show()
}

internal fun EditText.forceEditing() {
    requestFocus()
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        .showSoftInput(this, 0)
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
