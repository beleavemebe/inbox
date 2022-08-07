package io.github.beleavemebe.inbox.tasks.domain.model

import io.github.beleavemebe.inbox.tasks.domain.internal.OccurrencePatternImpl

interface OccurrencePattern {
    val intervals: List<Long>
    val stamps: List<Long>

    companion object {
        fun fromIntervals(intervals: List<Long>): OccurrencePattern {
            return OccurrencePatternImpl(convertIntervalsToStamps(intervals))
        }

        fun fromStamps(stamps: List<Long>): OccurrencePattern {
            return OccurrencePatternImpl(stamps)
        }

        fun convertStampsToIntervals(
            stamps: List<Long>
        ): List<Long> {
            val pattern = Array(stamps.size) { 0L }
            stamps.forEachIndexed { i, stamp ->
                if (i == 0) {
                    pattern[i] = stamp
                } else {
                    pattern[i] = stamp - stamps[i - 1]
                }
            }

            return pattern.toList()
        }

        fun convertIntervalsToStamps(
            intervals: List<Long>
        ): List<Long> {
            if (intervals.isEmpty()) return emptyList()
            val result = mutableListOf(intervals[0])
            return if (intervals.size == 1) {
                result
            } else {
                for (i in 1..intervals.lastIndex) {
                    result += result.last() + intervals[i]
                }
                result
            }
        }
    }
}
