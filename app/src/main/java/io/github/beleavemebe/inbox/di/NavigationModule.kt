package io.github.beleavemebe.inbox.di

import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.Module
import dagger.Provides
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.core.navigation.CrossFeatureNavigator
import io.github.beleavemebe.inbox.core.navigation.impl.NavComponentCrossFeatureNavigator
import io.github.beleavemebe.inbox.ui.MainActivity

@Module
object NavigationModule {
    @Provides
    fun provideNavController(
        mainActivity: MainActivity
    ): NavController {
        return mainActivity.supportFragmentManager
            .findFragmentById(R.id.fragment_container)!!
            .findNavController()
    }

    @Provides
    fun provideCrossFeatureNavigator(
        navController: NavController
    ): CrossFeatureNavigator {
        return NavComponentCrossFeatureNavigator(navController)
    }
}
