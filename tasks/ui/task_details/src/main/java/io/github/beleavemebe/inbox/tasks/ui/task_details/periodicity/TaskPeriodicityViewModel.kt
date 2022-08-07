package io.github.beleavemebe.inbox.tasks.ui.task_details.periodicity

import io.github.beleavemebe.inbox.tasks.domain.model.Periodicity
import kotlinx.coroutines.flow.Flow

interface TaskPeriodicityViewModel {
    val isPeriodicitySectionVisible: Flow<Boolean>

    fun setPeriodicityEnabled(enabled: Boolean)
    fun configurePeriodicity()
    fun setDailyPeriodicity()
    fun setWeeklyPeriodicity()
    fun setPeriodicity(periodicity: Periodicity)
}
