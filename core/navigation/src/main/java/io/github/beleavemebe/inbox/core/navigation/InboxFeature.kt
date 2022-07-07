package io.github.beleavemebe.inbox.core.navigation

sealed class InboxFeature {
    object Tasks : InboxFeature()
    object Projects : InboxFeature()
    object Schedule : InboxFeature()
}
