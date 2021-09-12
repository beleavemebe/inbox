package io.github.beleavemebe.inbox.util

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