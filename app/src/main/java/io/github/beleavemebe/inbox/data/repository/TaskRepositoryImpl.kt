package io.github.beleavemebe.inbox.data.repository

import io.github.beleavemebe.inbox.core.model.Task
import io.github.beleavemebe.inbox.core.model.TaskChecklist
import io.github.beleavemebe.inbox.core.repository.ChecklistRepository
import io.github.beleavemebe.inbox.core.repository.TaskRepository
import io.github.beleavemebe.inbox.data.db.ChecklistDao
import io.github.beleavemebe.inbox.data.db.TaskDao
import io.github.beleavemebe.inbox.data.db.TaskDatabase
import io.github.beleavemebe.inbox.data.model.*
import io.github.beleavemebe.inbox.data.model.mapper.toChecklistEntity
import io.github.beleavemebe.inbox.data.model.mapper.toTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    database: TaskDatabase,
) : TaskRepository, ChecklistRepository {
    private val taskDao: TaskDao = database.taskDao()
    private val checklistDao: ChecklistDao = database.checklistDao()

    override fun getTasks(): Flow<List<Task>> {
        return taskDao
            .getTasks()
            .map { dtoList ->
                dtoList.map(TaskDetails::toTask)
            }
    }

    override suspend fun getTaskById(id: UUID): Task {
        return taskDao.getTask(id).toTask()
    }

    override suspend fun insertTask(task: Task) {
        taskDao.insertTask(task.toTaskEntity())
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toTaskEntity())
    }

    override suspend fun deleteTask(taskToDelete: Task) {
        taskDao.deleteTask(taskToDelete.toTaskEntity())
    }

    override suspend fun insertChecklist(checklist: TaskChecklist) {
        checklistDao.insert(checklist.toChecklistEntity())
    }
}
