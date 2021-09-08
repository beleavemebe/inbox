package io.github.beleavemebe.inbox.model

class TaskRepeatFrequency(
    val freqMs: Long,
    val skipWeekends: Boolean
) {
    companion object {
        val REPEAT_DAILY    = TaskRepeatFrequency(86400000L,  false)
        val REPEAT_WEEKDAYS = TaskRepeatFrequency(86400000L,  true)
        val REPEAT_WEEKLY   = TaskRepeatFrequency(604800000L, false)
    }
}