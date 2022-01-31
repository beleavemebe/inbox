package io.github.beleavemebe.inbox.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import io.github.beleavemebe.inbox.ui.activities.MainActivity

abstract class DetailsFragment(@LayoutRes layoutResId: Int) : BaseFragment(layoutResId) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavMenu()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        revealBottomNavMenu()
    }

    private fun hideBottomNavMenu() {
        (requireActivity() as MainActivity)
            .hideBottomNavMenu()
    }

    private fun revealBottomNavMenu() {
        (requireActivity() as MainActivity)
            .revealBottomNavMenu()
    }
}
