package io.github.beleavemebe.inbox.ui.tasklist

import androidx.annotation.StringRes
import io.github.beleavemebe.inbox.R

enum class TaskFilterPreference(
    @StringRes val titleResId: Int
) {
    UNFILTERED(R.string.all),
    UNDATED(R.string.undated),
    DUE_THIS_WEEK(R.string.this_week),
    DUE_THIS_OR_NEXT_WEEK(R.string.this_or_next_week),
}
