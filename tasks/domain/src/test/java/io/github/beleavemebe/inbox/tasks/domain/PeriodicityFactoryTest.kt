package io.github.beleavemebe.inbox.tasks.domain

import io.github.beleavemebe.inbox.common.DAY_MS
import io.github.beleavemebe.inbox.common.HourAndMinute
import io.github.beleavemebe.inbox.common.WEEK_MS
import io.github.beleavemebe.inbox.common.lastMonday
import io.github.beleavemebe.inbox.tasks.domain.model.OccurrencePattern
import io.github.beleavemebe.inbox.tasks.domain.model.Periodicity
import org.junit.Assert
import org.junit.Test
import java.time.DayOfWeek


internal class PeriodicityFactoryTest {
    @Test
    fun `test creating daily period`() {
        val periodicity = Periodicity.Factory.newDailyPeriod(
            start = lastMonday,
            HourAndMinute(0 to 0)
        )

        val expected = Periodicity(
            start = lastMonday,
            duration = DAY_MS,
            occurrencePattern = OccurrencePattern.fromStamps(listOf(0))
        )

        Assert.assertTrue(periodicity == expected)
    }

    @Test
    fun `test creating weekly period`() {
        val periodicity = Periodicity.Factory.newWeeklyPeriodicity(
            start = lastMonday,
            DayOfWeek.TUESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SUNDAY
        )

        val expected = Periodicity(
            start = lastMonday,
            duration = WEEK_MS,
            occurrencePattern = OccurrencePattern.fromIntervals(
                listOf(
                    DAY_MS, // Tuesday
                    2 * DAY_MS, // Thursday
                    DAY_MS, // Friday
                    2 * DAY_MS, // Sunday
                )
            )
        )

        Assert.assertTrue(periodicity == expected)
    }
}
