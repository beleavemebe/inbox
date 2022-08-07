package io.github.beleavemebe.inbox.tasks.ui.task_details.checklist

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface TaskChecklistViewModel {
    val isChecklistSectionVisible: Flow<Boolean>

    fun setChecklistEnabled(enabled: Boolean)
    fun addChecklistEntry(context: Context)
    fun onChecklistItemTextChanged(index: Int, text: String)
    fun onChecklistItemChecked(index: Int, isChecked: Boolean)
}
