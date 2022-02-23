package io.github.beleavemebe.inbox.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::bind)

    private val navController: NavController by lazy {
        supportFragmentManager
            .findFragmentById(R.id.fragment_container)!!
            .findNavController()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        initToolbar()
        initBottomNavigation()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.mainToolbar)
        NavigationUI.setupActionBarWithNavController(this, navController, configureAppBar())
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

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun initBottomNavigation() {
        binding.mainBottomNavigationView.setOnItemReselectedListener {}
        binding.mainBottomNavigationView.setupWithNavController(navController)
    }

    fun hideBottomNavMenu() = setBottomNavVisible(false)
    fun revealBottomNavMenu() = setBottomNavVisible(true)

    private fun setBottomNavVisible(visible : Boolean) {
        binding.mainBottomNavigationView.isVisible = visible
    }
}

