package io.github.beleavemebe.inbox.tasks.ui.task_list

import androidx.annotation.StringRes

enum class TaskFilterPreference(
    @StringRes val titleResId: Int
) {
    UNFILTERED(R.string.all),
    UNDATED(R.string.undated),
    DUE_THIS_WEEK(R.string.this_week),
    DUE_THIS_OR_NEXT_WEEK(R.string.this_or_next_week),
}
