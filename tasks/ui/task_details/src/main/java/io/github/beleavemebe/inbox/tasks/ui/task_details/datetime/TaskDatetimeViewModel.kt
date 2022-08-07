package io.github.beleavemebe.inbox.tasks.ui.task_details.datetime

import io.github.beleavemebe.inbox.common.HourAndMinute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

interface TaskDatetimeViewModel {
    val isDatetimeSectionVisible: Flow<Boolean>
    val eventShowPickDateMenu: Flow<Boolean>
    val eventShowDatePickerDialog: Flow<Date>
    val eventShowDateNotSetToast: Flow<Boolean>
    val eventShowTimePickerDialog: Flow<HourAndMinute>

    fun setDatetimeEnabled(enabled: Boolean)

    fun pickDate()
    fun showDatePicker()
    fun setDate(ms: Long)

    fun pickTime()
    fun setTime(hour: Int, minute: Int)
    fun clearTime()
}
