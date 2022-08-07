package io.github.beleavemebe.inbox.tasks.domain.internal

import io.github.beleavemebe.inbox.tasks.domain.model.OccurrencePattern

internal data class OccurrencePatternImpl(
    override val stamps: List<Long>
) : OccurrencePattern {
    override val intervals by lazy { OccurrencePattern.convertStampsToIntervals(stamps) }
}
