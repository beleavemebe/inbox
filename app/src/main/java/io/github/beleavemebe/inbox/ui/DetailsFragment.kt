package io.github.beleavemebe.inbox.ui

import androidx.annotation.LayoutRes

abstract class DetailsFragment(@LayoutRes layoutResId: Int) : BaseFragment(layoutResId) {
    override fun onStart() {
        super.onStart()
        hideBottomNavMenu()
    }

    override fun onStop() {
        super.onStop()
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
