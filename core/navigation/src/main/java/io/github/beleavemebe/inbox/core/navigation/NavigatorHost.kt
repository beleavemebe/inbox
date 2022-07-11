package io.github.beleavemebe.inbox.core.navigation

import androidx.fragment.app.Fragment
import io.github.beleavemebe.inbox.core.utils.findInstanceInHierarchy

interface NavigatorHost {
    val navigator: CrossFeatureNavigator
}

val Fragment.crossFeatureNavigator: CrossFeatureNavigator
    get() = findInstanceInHierarchy<NavigatorHost>()
        .navigator
