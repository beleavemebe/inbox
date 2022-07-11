package io.github.beleavemebe.inbox.core.navigation

import java.util.*

sealed class InboxFeature {
    object TaskList : InboxFeature()
    data class TaskDetails(val id: UUID?, val title: String) : InboxFeature()
    object Projects : InboxFeature()
    object Schedule : InboxFeature()
}
