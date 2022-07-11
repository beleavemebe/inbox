package io.github.beleavemebe.inbox.core.navigation.impl

import androidx.navigation.NavController
import io.github.beleavemebe.inbox.core.navigation.CrossFeatureNavigator
import io.github.beleavemebe.inbox.core.navigation.InboxFeature
import io.github.beleavemebe.inbox.core.navigation.NavGraphDirections

class NavComponentCrossFeatureNavigator(
    private val navController: NavController
) : CrossFeatureNavigator {
    override fun navigateBack() {
        navController.navigateUp()
    }

    override fun navigateToFeature(feature: InboxFeature) {
        return when (feature) {
            is InboxFeature.TaskList -> navController.navigate(NavGraphDirections.actionGlobalToTaskList())
            is InboxFeature.TaskDetails -> {
                navController.navigate(NavGraphDirections.actionGlobalToTaskDetails(feature.id, feature.title))
            }
            is InboxFeature.Projects -> navController.navigate(NavGraphDirections.actionGlobalToProjects())
            is InboxFeature.Schedule -> navController.navigate(NavGraphDirections.actionGlobalToSchedule())
        }
    }
}
