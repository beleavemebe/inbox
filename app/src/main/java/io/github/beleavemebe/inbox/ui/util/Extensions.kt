package io.github.beleavemebe.inbox.ui.util

import android.content.Context
import android.graphics.Paint
import android.text.InputType
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

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

// https://stackoverflow.com/q/2986387
internal fun EditText.enableDoneImeAction() {
    imeOptions = EditorInfo.IME_ACTION_DONE
    setRawInputType(InputType.TYPE_CLASS_TEXT)
}

internal fun TextView.setCrossedOut(flag: Boolean) {
    if (flag)
        crossOut()
    else
        uncross()
}

internal val Fragment.actionBar: ActionBar
    get() = (requireActivity() as AppCompatActivity)
        .supportActionBar!!

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
