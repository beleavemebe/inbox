package io.github.beleavemebe.inbox.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding.mainBottomNavigationView.apply {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!
            NavigationUI.setupWithNavController(this, navHostFragment.findNavController())
        }
    }

    fun hideBottomNavMenu() {
        setBottomNavVisible(false)
    }

    fun revealBottomNavMenu() {
        setBottomNavVisible(true)
    }

    fun getBottomNavigationView() = binding.mainBottomNavigationView

    private fun setBottomNavVisible(visibility : Boolean) {
        binding.mainBottomNavigationView.isVisible = visibility
    }
}
