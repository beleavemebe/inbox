package io.github.beleavemebe.inbox.tasks.ui.task_details.periodicity

import android.content.Context
import io.github.beleavemebe.inbox.core.utils.setVisibleAnimated
import io.github.beleavemebe.inbox.tasks.ui.task_details.R

class DailyPeriodicityUiState(private val context: Context) : PeriodicityUiState {
    override fun render(ui: PeriodicityUi) {
        ui.tvPeriodicity.text = context.getString(R.string.daily)
        ui.groupWeeklyPeriodicityCheckboxes.setVisibleAnimated(false)
    }
}
