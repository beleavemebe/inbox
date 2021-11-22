package io.github.beleavemebe.inbox.db.task

import androidx.lifecycle.LiveData
import androidx.room.*
import io.github.beleavemebe.inbox.model.Task
import java.util.*

@Dao
interface TaskDao {
    @Query("SELECT * FROM task WHERE id=(:id)")
    fun getTask(id : UUID): LiveData<Task?>

    @Query("SELECT * FROM task ORDER BY is_completed ASC, date IS NULL ASC, date ASC, timestamp ASC")
    fun getTasks(): LiveData<MutableList<Task>>

    @Update
    fun updateTask(task: Task)

    @Insert
    fun addTask(task: Task)

    @Delete
    fun deleteTask(taskToDelete: Task)

    @Query("DELETE FROM task WHERE id=(:taskId)")
    fun deleteTask(taskId: UUID)

    @Query("DELETE FROM task WHERE is_completed=1")
    fun deleteCompletedTasks()
}
