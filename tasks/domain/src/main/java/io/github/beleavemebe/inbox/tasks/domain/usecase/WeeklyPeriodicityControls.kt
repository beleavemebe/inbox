package io.github.beleavemebe.inbox.tasks.domain.usecase

import io.github.beleavemebe.inbox.common.DAY_MS
import io.github.beleavemebe.inbox.common.WEEK_MS
import io.github.beleavemebe.inbox.tasks.domain.model.Periodicity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.DayOfWeek

class WeeklyPeriodicityControls(
    private var periodicity: Periodicity
) {
    fun getPeriodicity() = periodicity

    private val _daysSelected = MutableStateFlow(periodicity.getDaysSelected())
    val daysSelected = _daysSelected.asStateFlow()

    fun setDaySelected(dayOfWeek: DayOfWeek, selected: Boolean) {
        if (selected) {
            includeDayOfWeek(dayOfWeek)
        } else {
            excludeDayOfWeek(dayOfWeek)
        }
    }

    private fun includeDayOfWeek(dayOfWeek: DayOfWeek) {
        val daysSelected = periodicity.getDaysSelected()
        if (dayOfWeek in daysSelected) {
            error("Day of week $dayOfWeek is already included")
        }

        val newDaysOfWeek = daysSelected + dayOfWeek
        updateState(newDaysOfWeek)
    }

    private fun excludeDayOfWeek(dayOfWeek: DayOfWeek) {
        val daysSelected = periodicity.getDaysSelected()
        if (dayOfWeek !in daysSelected) {
            error("Day of week $dayOfWeek is already excluded")
        }

        val newDaysOfWeek = daysSelected - dayOfWeek
        updateState(newDaysOfWeek)
    }

    private fun updateState(newDaysOfWeek: List<DayOfWeek>) {
        periodicity = Periodicity.Factory.newWeeklyPeriodicity(periodicity.start, newDaysOfWeek)
        _daysSelected.value = newDaysOfWeek
    }

    private fun Periodicity.getDaysSelected(): List<DayOfWeek> {
        require(duration == WEEK_MS)
        val stamps = occurrencePattern.stamps

        val isMondaySelected    = isMondaySelected(stamps)
        val isTuesdaySelected   = isTuesdaySelected(stamps)
        val isWednesdaySelected = isWednesdaySelected(stamps)
        val isThursdaySelected  = isThursdaySelected(stamps)
        val isFridaySelected    = isFridaySelected(stamps)
        val isSaturdaySelected  = isSaturdaySelected(stamps)
        val isSundaySelected    = isSundaySelected(stamps)

        return listOfNotNull(
            if (isMondaySelected) DayOfWeek.MONDAY else null,
            if (isTuesdaySelected) DayOfWeek.TUESDAY else null,
            if (isWednesdaySelected) DayOfWeek.WEDNESDAY else null,
            if (isThursdaySelected) DayOfWeek.THURSDAY else null,
            if (isFridaySelected) DayOfWeek.FRIDAY else null,
            if (isSaturdaySelected) DayOfWeek.SATURDAY else null,
            if (isSundaySelected) DayOfWeek.SUNDAY else null
        )
    }

    private fun isMondaySelected(stamps: List<Long>): Boolean =
        stamps.any { it in 0 until DAY_MS }

    private fun isTuesdaySelected(stamps: List<Long>): Boolean =
        stamps.any { it in 1 * DAY_MS until 2 * DAY_MS }

    private fun isWednesdaySelected(stamps: List<Long>): Boolean =
        stamps.any { it in 2 * DAY_MS until 3 * DAY_MS }

    private fun isThursdaySelected(stamps: List<Long>): Boolean =
        stamps.any { it in 3 * DAY_MS until 4 * DAY_MS }

    private fun isFridaySelected(stamps: List<Long>): Boolean =
        stamps.any { it in 4 * DAY_MS until 5 * DAY_MS }

    private fun isSaturdaySelected(stamps: List<Long>): Boolean =
        stamps.any { it in 5 * DAY_MS until 6 * DAY_MS }

    private fun isSundaySelected(stamps: List<Long>): Boolean =
        stamps.any { it in 6 * DAY_MS until WEEK_MS }

}
