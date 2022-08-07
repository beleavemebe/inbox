package io.github.beleavemebe.inbox.tasks.domain.model

import io.github.beleavemebe.inbox.common.DAY_MS
import io.github.beleavemebe.inbox.common.HourAndMinute
import io.github.beleavemebe.inbox.common.WEEK_MS
import io.github.beleavemebe.inbox.common.dayOfWeek
import java.time.DayOfWeek
import java.util.*

data class Periodicity(
    val start: Long,
    val duration: Long,
    val occurrencePattern: OccurrencePattern
) {
    val lastInterval = duration - occurrencePattern.intervals.sum()

    object Factory {
        fun newDailyPeriod(
            start: Long,
            hourAndMinute: HourAndMinute
        ): Periodicity {
            return Periodicity(
                start = start,
                duration = DAY_MS,
                OccurrencePattern.fromStamps(listOf(hourAndMinute.toMillis()))
            )
        }

        fun newWeeklyPeriodicity(
            start: Long,
            vararg daysOfWeek: DayOfWeek,
        ): Periodicity {
            require(daysOfWeek.size <= 7) {
                "Weekly patterns can't have more than 7 occurrences"
            }

            require(Date(start).dayOfWeek == Calendar.MONDAY) {
                "Weekly patterns must start on mondays"
            }

            val dayOfWeekStamps = daysOfWeek.map { it.ordinal * DAY_MS }.sorted()
            return Periodicity(
                start,
                WEEK_MS,
                OccurrencePattern.fromStamps(dayOfWeekStamps)
            )
        }

        fun newWeeklyPeriodicity(
            start: Long,
            daysOfWeek: List<DayOfWeek>,
        ): Periodicity = newWeeklyPeriodicity(start, *daysOfWeek.toTypedArray())
    }

}
