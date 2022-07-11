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

    override val toolbar get() = binding.mainToolbar

    @Inject lateinit var navController: NavController
    @Inject override lateinit var navigator: CrossFeatureNavigator

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
        setSupportActionBar(binding.mainToolbar)
        binding.mainToolbar.setupWithNavController(navController, binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun initNavigationView() {
        binding.navView.setupWithNavController(navController)
    }
}

