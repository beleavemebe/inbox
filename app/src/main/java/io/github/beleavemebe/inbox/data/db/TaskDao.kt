package io.github.beleavemebe.inbox.data.db

import androidx.room.*
import io.github.beleavemebe.inbox.data.model.TaskDetails
import io.github.beleavemebe.inbox.data.model.TaskEntity
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface TaskDao {
    @Transaction
    @Query("SELECT * FROM task")
    fun getTasks(): Flow<List<TaskDetails>>

    @Transaction
    @Query("SELECT * FROM task WHERE id=(:id)")
    suspend fun getTask(id: UUID): TaskDetails

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Insert
    suspend fun insertTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(taskToDelete: TaskEntity)
}
