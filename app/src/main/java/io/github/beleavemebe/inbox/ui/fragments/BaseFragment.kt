package io.github.beleavemebe.inbox.ui.fragments

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment(@LayoutRes layoutResId: Int) : Fragment(layoutResId) {
    protected abstract fun adaptMainToolbar()

    override fun onResume() {
        super.onResume()
        adaptMainToolbar()
    }
}