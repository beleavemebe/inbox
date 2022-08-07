package io.github.beleavemebe.inbox.tasks.ui.task_details.periodicity

import android.content.Context
import androidx.lifecycle.Lifecycle
import io.github.beleavemebe.inbox.common.DAY_MS
import io.github.beleavemebe.inbox.common.WEEK_MS
import io.github.beleavemebe.inbox.tasks.domain.model.Periodicity
import io.github.beleavemebe.inbox.tasks.domain.usecase.WeeklyPeriodicityControls
import javax.inject.Inject

class PeriodicityUiStateFactory @Inject constructor(
    private val context: Context,
    private val lifecycle: Lazy<Lifecycle>,
) : PeriodicityUiState.Factory {
    override fun createInitialState(
        periodicity: Periodicity?
    ): PeriodicityUiState {
        return when {
            periodicity == null -> BlankPeriodicityUiState()
            periodicity.duration == DAY_MS -> DailyPeriodicityUiState(context)
            periodicity.duration == WEEK_MS ->
                WeeklyPeriodicityUiState(
                    context, lifecycle.value, WeeklyPeriodicityControls(periodicity)
                )
            else -> error("Could not recognize periodicity of duration ${periodicity.duration}")
        }
    }
}
