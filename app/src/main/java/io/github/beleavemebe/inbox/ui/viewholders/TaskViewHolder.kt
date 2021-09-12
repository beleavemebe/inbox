package io.github.beleavemebe.inbox.ui.viewholders

import android.graphics.Paint
import android.util.Log
import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.databinding.ListItemTaskBinding
import io.github.beleavemebe.inbox.model.Task
import io.github.beleavemebe.inbox.repositories.TaskRepository
import io.github.beleavemebe.inbox.ui.fragments.tasklist.TaskListFragmentDirections

const val TAG = "TaskViewHolder"

class TaskViewHolder(taskView: View) :
    RecyclerView.ViewHolder(taskView),
    View.OnClickListener
{
    private lateinit var task: Task
    private val repo get() = TaskRepository.getInstance()
    private var binding: ListItemTaskBinding = ListItemTaskBinding.bind(taskView)

    fun bind(task: Task) {
        this.task = task
        Log.d(TAG, "i bind task: $task")
        initTitleTv(task)
        initDeleteIb(task)
        initCompletedCb(task)
    }

    private fun initTitleTv(task: Task) = with (binding.taskTitleTv) {
        text = task.title
        alterViewIfTaskIsCompleted()
    }

    private fun initCompletedCb(task: Task) = with (binding) {
        completedCb.apply {
            isChecked = task.isCompleted
            jumpDrawablesToCurrentState()
            setOnCheckedChangeListener { _, isChecked ->
                Log.d(TAG, "task checked")
                task.isCompleted = isChecked
                repo.updateTask(task)
                alterViewIfTaskIsCompleted()
            }
        }
    }

    private fun alterViewIfTaskIsCompleted() = with (binding.taskTitleTv) {
        val textColor : Int
        if (task.isCompleted) {
            textColor = resources.getColor(R.color.secondary_text)
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            textColor = resources.getColor(R.color.primary_text)
            paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
        setTextColor(textColor)
    }

    private fun initDeleteIb(task: Task) = with (binding) {
        deleteIb.setOnClickListener {
            root.setOnClickListener(null)
            repo.deleteTask(task)
        }
    }

    init {
        taskView.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        Log.d(TAG, "navigating to task $task")
        view!!.findNavController().navigate(
                TaskListFragmentDirections.actionTaskListFragmentToTaskFragment(task.id)
            )
    }
}