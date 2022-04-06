package io.github.beleavemebe.inbox.ui.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Paint
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

@Suppress("unused")
fun Any.log(msg: Any?) = Log.d("inbox-debug", msg.toString())

fun Context?.toast(@StringRes stringRes: Int) {
    if (this == null) return
    toast(getString(stringRes))
}

fun Context?.toast(text: String) {
    Toast.makeText(
        this ?: return,
        text,
        Toast.LENGTH_SHORT
    ).show()
}

inline fun <reified T> Context.getSystemService(): T? {
    return ContextCompat.getSystemService(this, T::class.java)
}

fun EditText.forceEditing() {
    requestFocus()
    context.getSystemService<InputMethodManager>()
        ?.showSoftInput(this, 0)
}

fun EditText.hideKeyboard() {
    context.getSystemService<InputMethodManager>()
        ?.hideSoftInputFromWindow(this.windowToken, 0)
}

// https://stackoverflow.com/q/2986387
fun EditText.enableDoneImeAction() {
    imeOptions = EditorInfo.IME_ACTION_DONE
    setRawInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_CLASS_TEXT)
}

fun TextView.setCrossedOut(flag: Boolean) {
    if (flag) crossOut()
    else uncross()
}

fun Group.setVisibleAnimated(flag: Boolean) {
    referencedIds.forEach { viewId ->
        rootView.findViewById<View>(viewId)
            ?.setVisibleAnimated(flag)
    }
}

fun View.setVisibleAnimated(flag: Boolean) {
    if (flag) {
        isVisible = true
        alpha = 0f
        animate().alpha(1f)
    } else {
        isVisible = false
    }
}

val Fragment.actionBar: ActionBar
    get() = (requireActivity() as AppCompatActivity)
        .supportActionBar!!

private fun TextView.crossOut() {
    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

private fun TextView.uncross() {
    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
}

@ColorInt
fun Resources.getColorCompat(
    context: Context,
    @ColorRes colorResId: Int
): Int =
    ResourcesCompat.getColor(
        this,
        colorResId,
        context.applicationContext.theme
    )

fun <E> MutableList<E>.refillWith(content: List<E>) {
    clear()
    addAll(content)
}

fun Int.toDoubleFiguredString() = if (this >= 10) "$this" else "0${this}"
