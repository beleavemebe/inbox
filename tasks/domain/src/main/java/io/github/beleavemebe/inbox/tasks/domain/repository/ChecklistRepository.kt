package io.github.beleavemebe.inbox.tasks.domain.repository

import io.github.beleavemebe.inbox.tasks.domain.model.TaskChecklist

interface ChecklistRepository {
    suspend fun insertChecklist(checklist: TaskChecklist)
}
