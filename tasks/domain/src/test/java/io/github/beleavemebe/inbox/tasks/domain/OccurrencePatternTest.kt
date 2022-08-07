package io.github.beleavemebe.inbox.tasks.domain

import io.github.beleavemebe.inbox.common.DAY_MS
import io.github.beleavemebe.inbox.tasks.domain.model.OccurrencePattern
import org.junit.Test

internal class OccurrencePatternTest {
    @Test
    fun `test conversion utilities`() {
        // Test with weekly pattern - [Tue, Thu, Fri, Sun]
        val stamps = listOf(86_400_000L, 259_200_000L, 345_600_000L, 518_400_000L)
        val intervals = listOf(DAY_MS, 2 * DAY_MS, DAY_MS, 2 * DAY_MS)

        AssertExt.assertListEquals(
            intervals,
            OccurrencePattern.convertStampsToIntervals(stamps)
        )

        AssertExt.assertListEquals(
            stamps,
            OccurrencePattern.convertIntervalsToStamps(intervals)
        )
    }
}
