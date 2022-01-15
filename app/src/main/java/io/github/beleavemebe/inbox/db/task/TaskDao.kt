package io.github.beleavemebe.inbox.db.task

import androidx.lifecycle.LiveData
import androidx.room.*
import io.github.beleavemebe.inbox.model.Task
import java.util.*

@Dao
interface TaskDao {
    @Query("SELECT * FROM task WHERE id=(:id)")
    fun getTask(id : UUID): LiveData<Task>

    @Query("SELECT * FROM task ORDER BY is_completed ASC, date IS NULL ASC, date ASC, timestamp ASC")
    fun getTasks(): LiveData<List<Task>>

    @Update
    suspend fun updateTask(task: Task)

    @Insert
    suspend fun addTask(task: Task)

    @Delete
    suspend fun deleteTask(taskToDelete: Task)
}
