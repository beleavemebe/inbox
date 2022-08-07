package io.github.beleavemebe.inbox.tasks.domain.model

import java.util.Date

class PeriodicityCalendar(
    private val periodicity: Periodicity
) {
    /**
     * Find an occurrence after specified date (exclusive)
     *
     * @param date Date, after which next reoccurrence is sought
     * @return Date of the nearest next reoccurrence
     */
    fun getOccurrenceAfter(date: Date): Date {
        val startOffset = periodicity.duration * ((date.time - periodicity.start) / periodicity.duration)
        var occurrenceMillis = periodicity.start + startOffset

        // Naive O(n) implementation - just walk through occurrences
        val intervals = periodicity.occurrencePattern.intervals
        var nextIntervalIndex = 0
        while (occurrenceMillis <= date.time) {
            if (nextIntervalIndex == intervals.size) {
                occurrenceMillis += periodicity.lastInterval
                nextIntervalIndex = 0
            }

            val interval = intervals[nextIntervalIndex++]
            occurrenceMillis += interval
        }

        return Date(occurrenceMillis)
    }
}
