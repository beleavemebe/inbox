package io.github.beleavemebe.inbox.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import by.kirich1409.viewbindingdelegate.viewBinding
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding.mainBottomNavigationView.apply {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)!!
            NavigationUI.setupWithNavController(this, navHostFragment.findNavController())
        }
    }

    val mainToolbar get() = binding.mainToolbar
    val bottomNavigationView get() = binding.mainBottomNavigationView

    fun hideBottomNavMenu() {
        setBottomNavVisible(false)
    }

    fun revealBottomNavMenu() {
        setBottomNavVisible(true)
    }

    private fun setBottomNavVisible(visible : Boolean) =
        with (binding) {
            mainBottomNavigationView.isVisible = visible
//            fragmentContainer.bottomMargin = if (visible) 56 else 0
        }
}
