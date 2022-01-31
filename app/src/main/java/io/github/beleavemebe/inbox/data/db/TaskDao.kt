package io.github.beleavemebe.inbox.data.db

import androidx.room.*
import io.github.beleavemebe.inbox.data.model.TaskEntity
import java.util.*

@Dao
interface TaskDao {
    @Query("SELECT * FROM task WHERE id=(:id)")
    suspend fun getTask(id : UUID): TaskEntity

    @Query("SELECT * FROM task ORDER BY is_completed ASC, date IS NULL ASC, date ASC, timestamp ASC")
    suspend fun getTasks(): List<TaskEntity>

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Insert
    suspend fun addTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(taskToDelete: TaskEntity)
}
