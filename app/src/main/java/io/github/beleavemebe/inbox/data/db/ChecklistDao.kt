package io.github.beleavemebe.inbox.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import io.github.beleavemebe.inbox.data.model.TaskChecklistEntity

@Dao
interface ChecklistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(checklistEntity: TaskChecklistEntity)
}
