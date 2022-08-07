package io.github.beleavemebe.inbox.tasks.domain

import io.github.beleavemebe.inbox.common.HourAndMinute
import io.github.beleavemebe.inbox.common.dayOfWeek
import io.github.beleavemebe.inbox.common.lastMonday
import io.github.beleavemebe.inbox.tasks.domain.model.Periodicity
import io.github.beleavemebe.inbox.tasks.domain.model.PeriodicityCalendar
import org.junit.Test
import java.time.DayOfWeek
import java.util.*

internal class PeriodicityCalendarTest {
    @Test
    fun `test daily pattern`() {
        val periodicity = Periodicity.Factory.newDailyPeriod(
            start = lastMonday,
            HourAndMinute(0 to 0)
        )
        val periodicityCalendar = PeriodicityCalendar(periodicity)
        val occurrences = mutableListOf(periodicityCalendar.getOccurrenceAfter(Date(lastMonday)))
        for (i in 1..5) {
            occurrences += periodicityCalendar.getOccurrenceAfter(occurrences[i - 1])
        }

        val mapped = occurrences.map { it.dayOfWeek }
        AssertExt.assertListEquals(
            mapped, listOf(
                Calendar.TUESDAY,
                Calendar.WEDNESDAY,
                Calendar.THURSDAY,
                Calendar.FRIDAY,
                Calendar.SATURDAY,
                Calendar.SUNDAY
            )
        )
    }

    @Test
    fun `test weekly pattern`() {
        val periodicity = Periodicity.Factory.newWeeklyPeriodicity(
            start = lastMonday,
            DayOfWeek.TUESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SUNDAY
        )
        val periodicityCalendar = PeriodicityCalendar(periodicity)
        val occurrences = mutableListOf(periodicityCalendar.getOccurrenceAfter(Date(lastMonday)))
        for (i in 1..5) {
            occurrences += periodicityCalendar.getOccurrenceAfter(occurrences[i - 1])
        }

        val mapped = occurrences.map { it.dayOfWeek }
        AssertExt.assertListEquals(
            mapped, listOf(
                Calendar.TUESDAY,
                Calendar.THURSDAY,
                Calendar.FRIDAY,
                Calendar.SUNDAY,
                Calendar.TUESDAY,
                Calendar.THURSDAY
            )
        )
    }
}
