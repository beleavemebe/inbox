package io.github.beleavemebe.inbox.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.core.navigation.CrossFeatureNavigator
import io.github.beleavemebe.inbox.core.navigation.NavigatorHost
import io.github.beleavemebe.inbox.core.ui.ToolbarHost
import io.github.beleavemebe.inbox.databinding.ActivityMainBinding
import io.github.beleavemebe.inbox.di.DaggerMainActivityComponent
import javax.inject.Inject

class MainActivity : AppCompatActivity(), ToolbarHost, NavigatorHost {
    private val binding by viewBinding(ActivityMainBinding::bind)

    @Inject lateinit var navController: NavController
    @Inject lateinit var _navigator: CrossFeatureNavigator

    override val toolbar get() = binding.mainToolbar
    override val navigator get() = _navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DaggerMainActivityComponent.factory()
            .create(this)
            .inject(this)

        initToolbar()
        initNavigationView()
    }

    private fun initToolbar() {
        binding.mainToolbar.setupWithNavController(navController, binding.root)
        setSupportActionBar(binding.mainToolbar)
        // Drawer button is disabled, gotta call setup again xd
        binding.mainToolbar.setupWithNavController(navController, binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun initNavigationView() {
        binding.navView.setupWithNavController(navController)
    }
}

