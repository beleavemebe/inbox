package io.github.beleavemebe.inbox.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.core.ui.ToolbarOwner
import io.github.beleavemebe.inbox.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ToolbarOwner {
    private val binding by viewBinding(ActivityMainBinding::bind)

    private val navController: NavController by lazy {
        supportFragmentManager
            .findFragmentById(R.id.fragment_container)!!
            .findNavController()
    }

    override val toolbar get() = binding.mainToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        initToolbar()
        initNavigationView()
    }

    private fun initToolbar() {
        binding.mainToolbar.setupWithNavController(navController, binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun initNavigationView() {
        binding.navView.setupWithNavController(navController)
    }
}

