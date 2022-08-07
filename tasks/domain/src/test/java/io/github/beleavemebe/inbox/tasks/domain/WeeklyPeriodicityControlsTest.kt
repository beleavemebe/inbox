package io.github.beleavemebe.inbox.tasks.domain

import io.github.beleavemebe.inbox.common.lastMonday
import io.github.beleavemebe.inbox.tasks.domain.model.Periodicity
import io.github.beleavemebe.inbox.tasks.domain.usecase.WeeklyPeriodicityControls
import org.junit.Assert
import org.junit.Test
import java.time.DayOfWeek

internal class WeeklyPeriodicityControlsTest {

    private val periodicity = Periodicity.Factory.newWeeklyPeriodicity(
        start = lastMonday,
        DayOfWeek.TUESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SUNDAY
    )


    @Test
    fun `test Tue, Thu, Fri, Sun pattern`() {
        val weeklyPeriodicityControls = WeeklyPeriodicityControls(periodicity)

        Assert.assertTrue(periodicity == weeklyPeriodicityControls.getPeriodicity())

        AssertExt.assertListEquals(
            weeklyPeriodicityControls.daysSelected.value,
            listOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SUNDAY)
        )

        weeklyPeriodicityControls.setDaySelected(DayOfWeek.TUESDAY, false)
        AssertExt.assertListEquals(
            weeklyPeriodicityControls.daysSelected.value,
            listOf(DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SUNDAY)
        )
        Assert.assertEquals(
            weeklyPeriodicityControls.getPeriodicity(),
            Periodicity.Factory.newWeeklyPeriodicity(
                start = lastMonday, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SUNDAY
            )
        )

        // some more tests please
    }
}
