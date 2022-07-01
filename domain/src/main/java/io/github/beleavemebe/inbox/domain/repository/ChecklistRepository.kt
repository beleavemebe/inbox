package io.github.beleavemebe.inbox.domain.repository

import io.github.beleavemebe.inbox.domain.model.TaskChecklist

interface ChecklistRepository {
    suspend fun insertChecklist(checklist: TaskChecklist)
}
