package io.github.beleavemebe.inbox.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import by.kirich1409.viewbindingdelegate.viewBinding
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.databinding.ActivityMainBinding
import io.github.beleavemebe.inbox.ui.fragments.BaseFragment

class MainActivity : AppCompatActivity() {
    val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)

        val navController: NavController = supportFragmentManager
            .findFragmentById(R.id.fragment_container)!!
            .findNavController()

        initToolbar(navController)
        initBottomNavigation(navController)
    }

    private fun initToolbar(navController: NavController) {
        NavigationUI.setupWithNavController(
            binding.mainToolbar,
            navController,
            configureAppBar()
        )
    }

    private fun configureAppBar(): AppBarConfiguration {
        val topLevelDestinations = setOf(
            R.id.taskListFragment,
            R.id.projectListFragment,
            R.id.infoListFragment,
            R.id.meetingListFragment,
        )

        return AppBarConfiguration(topLevelDestinations)
    }

    private fun initBottomNavigation(navController: NavController) {
        NavigationUI.setupWithNavController(
            binding.mainBottomNavigationView,
            navController
        )
    }

    fun hideBottomNavMenu() { setBottomNavVisible(false) }
    fun revealBottomNavMenu() { setBottomNavVisible(true) }

    private fun setBottomNavVisible(visible : Boolean) {
        binding.mainBottomNavigationView.isVisible = visible
    }

    companion object {
        val BaseFragment.mainBottomNavigationView
            get() = (requireActivity() as MainActivity)
                .binding.mainBottomNavigationView

        fun BaseFragment.hideBottomNavMenu() {
            (requireActivity() as MainActivity)
                .hideBottomNavMenu()
        }

        fun BaseFragment.revealBottomNavMenu() {
            (requireActivity() as MainActivity)
                .revealBottomNavMenu()
        }
    }
}

