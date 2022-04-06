package io.github.beleavemebe.inbox.core.repository

import io.github.beleavemebe.inbox.core.model.TaskChecklist

interface ChecklistRepository {
    suspend fun insertChecklist(checklist: TaskChecklist)
}
