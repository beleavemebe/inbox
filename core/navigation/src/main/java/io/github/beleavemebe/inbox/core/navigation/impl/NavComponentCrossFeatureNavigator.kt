package io.github.beleavemebe.inbox.core.navigation.impl

import androidx.navigation.NavController
import io.github.beleavemebe.inbox.core.navigation.CrossFeatureNavigator
import io.github.beleavemebe.inbox.core.navigation.InboxFeature
import io.github.beleavemebe.inbox.core.navigation.NavGraphDirections

class NavComponentCrossFeatureNavigator(
    private val navController: NavController
) : CrossFeatureNavigator {
    override fun navigateToFeature(feature: InboxFeature) {
        when (feature) {
            InboxFeature.Tasks -> navController.navigate(NavGraphDirections.actionGlobalToTasks())
            InboxFeature.Projects -> navController.navigate(NavGraphDirections.actionGlobalToProjects())
            InboxFeature.Schedule -> navController.navigate(NavGraphDirections.actionGlobalToSchedule())
        }
    }
}