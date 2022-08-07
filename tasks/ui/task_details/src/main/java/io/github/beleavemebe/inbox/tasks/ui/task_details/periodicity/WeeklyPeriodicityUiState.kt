package io.github.beleavemebe.inbox.tasks.ui.task_details.periodicity

import android.content.Context
import android.widget.CheckBox
import androidx.lifecycle.Lifecycle
import io.github.beleavemebe.inbox.core.utils.repeatWhenStarted
import io.github.beleavemebe.inbox.core.utils.setVisibleAnimated
import io.github.beleavemebe.inbox.tasks.domain.usecase.WeeklyPeriodicityControls
import io.github.beleavemebe.inbox.tasks.ui.task_details.R
import kotlinx.coroutines.flow.onEach
import java.time.DayOfWeek

class WeeklyPeriodicityUiState(
    private val context: Context,
    private val lifecycle: Lifecycle,
    private val weeklyPeriodicityControls: WeeklyPeriodicityControls,
) : PeriodicityUiState {
    override fun render(ui: PeriodicityUi) {
        ui.tvPeriodicity.text = context.getString(R.string.weekly)
        ui.groupWeeklyPeriodicityCheckboxes.setVisibleAnimated(true)
        weeklyPeriodicityControls.daysSelected
            .onEach { daysSelected ->
                renderCheckbox(ui.cbPeriodicityMon, DayOfWeek.MONDAY, daysSelected)
                renderCheckbox(ui.cbPeriodicityTue, DayOfWeek.TUESDAY, daysSelected)
                renderCheckbox(ui.cbPeriodicityWed, DayOfWeek.WEDNESDAY, daysSelected)
                renderCheckbox(ui.cbPeriodicityThu, DayOfWeek.THURSDAY, daysSelected)
                renderCheckbox(ui.cbPeriodicityFri, DayOfWeek.FRIDAY, daysSelected)
                renderCheckbox(ui.cbPeriodicitySat, DayOfWeek.SATURDAY, daysSelected)
                renderCheckbox(ui.cbPeriodicitySun, DayOfWeek.SUNDAY, daysSelected)
            }
            .repeatWhenStarted(lifecycle)
    }

    private fun renderCheckbox(
        checkbox: CheckBox,
        dayOfWeek: DayOfWeek,
        daysSelected: List<DayOfWeek>,
    ) = with(checkbox) {
        setOnCheckedChangeListener(null)
        isChecked = dayOfWeek in daysSelected
        setOnCheckedChangeListener { _, isChecked ->
            weeklyPeriodicityControls.setDaySelected(dayOfWeek, isChecked)
        }
    }
}
