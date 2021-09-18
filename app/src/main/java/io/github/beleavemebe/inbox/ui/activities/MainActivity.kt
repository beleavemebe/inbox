package io.github.beleavemebe.inbox.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.github.beleavemebe.inbox.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavView = findViewById<BottomNavigationView>(R.id.main_bottom_navigation_view)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!
        NavigationUI.setupWithNavController(bottomNavView, navHostFragment.findNavController())
    }

    fun hideBottomNavMenu() {
        setBottomNavVisibility(false)
    }

    fun revealBottomNavMenu() {
        setBottomNavVisibility(true)
    }

    private fun setBottomNavVisibility(visibility : Boolean) {
        findViewById<View>(R.id.main_bottom_navigation_view).apply {
            this.isVisible = visibility
        }
    }
}