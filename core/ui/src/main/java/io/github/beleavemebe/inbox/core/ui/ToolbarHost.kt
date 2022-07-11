package io.github.beleavemebe.inbox.core.ui

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import io.github.beleavemebe.inbox.core.utils.findInstanceInHierarchy

interface ToolbarHost {
    val toolbar: Toolbar
}

val Fragment.toolbar: Toolbar
    get() = findInstanceInHierarchy<ToolbarHost>()
        .toolbar
