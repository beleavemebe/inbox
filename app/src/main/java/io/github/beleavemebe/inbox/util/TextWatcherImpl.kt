package io.github.beleavemebe.inbox.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

internal fun EditText.addOnTextChangedListener(onTextChangedAction: (CharSequence) -> Unit) {
    this.addTextChangedListener(
        TextWatcherImpl.newWatcher(onTextChangedAction)
    )
}

internal class TextWatcherImpl(private val onTextChangedAction: (CharSequence) -> Unit) : TextWatcher {
    companion object {
        fun newWatcher(onTextChangedAction: (CharSequence) -> Unit) =
            TextWatcherImpl(onTextChangedAction)
    }

    override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) =
        onTextChangedAction.invoke(sequence ?: "")

    override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(sequence: Editable?) {}
}
