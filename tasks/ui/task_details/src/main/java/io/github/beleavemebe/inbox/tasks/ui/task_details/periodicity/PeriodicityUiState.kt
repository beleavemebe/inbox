package io.github.beleavemebe.inbox.tasks.ui.task_details.periodicity

import io.github.beleavemebe.inbox.tasks.domain.model.Periodicity

interface PeriodicityUiState {
    fun render(ui: PeriodicityUi)

    interface Factory {
        fun createInitialState(
            periodicity: Periodicity?,
        ): PeriodicityUiState
    }
}
