package io.github.beleavemebe.inbox.util

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes

internal fun log(msg: Any?) = Log.d("app-debug", msg.toString())

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

internal fun TextView.setCrossedOut(flag: Boolean) {
    if (flag)
        crossOut()
    else
        uncross()
}

private fun TextView.crossOut() {
    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

private fun TextView.uncross() {
    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
}

fun <E> MutableList<E>.refillWith(content: List<E>) {
    clear()
    addAll(content)
}
