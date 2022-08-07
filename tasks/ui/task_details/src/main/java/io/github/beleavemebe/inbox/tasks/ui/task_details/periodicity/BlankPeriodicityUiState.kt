package io.github.beleavemebe.inbox.tasks.ui.task_details.periodicity

import io.github.beleavemebe.inbox.core.utils.setVisibleAnimated

class BlankPeriodicityUiState : PeriodicityUiState {
    override fun render(ui: PeriodicityUi) {
        ui.tvPeriodicity.text = ""
        ui.groupWeeklyPeriodicityCheckboxes.setVisibleAnimated(false)
    }
}
