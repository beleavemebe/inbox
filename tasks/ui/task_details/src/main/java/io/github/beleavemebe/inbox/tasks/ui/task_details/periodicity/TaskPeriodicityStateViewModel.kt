package io.github.beleavemebe.inbox.tasks.ui.task_details.periodicity

import io.github.beleavemebe.inbox.tasks.domain.model.Periodicity
import kotlinx.coroutines.flow.Flow

interface TaskPeriodicityStateViewModel {
    val uiState: Flow<PeriodicityUiState>

    fun setPeriodicityEnabled(enabled: Boolean)
    fun configurePeriodicity()
    fun setDailyPeriodicity()
    fun setWeeklyPeriodicity()
    fun setPeriodicity(periodicity: Periodicity)
}
