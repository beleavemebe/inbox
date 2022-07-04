package io.github.beleavemebe.inbox.core.ui

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

interface ToolbarOwner {
    val toolbar: Toolbar
}

val Fragment.toolbar: Toolbar
    get() = (requireActivity() as ToolbarOwner)
        .toolbar