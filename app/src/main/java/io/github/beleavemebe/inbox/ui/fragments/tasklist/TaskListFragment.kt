package io.github.beleavemebe.inbox.ui.fragments.tasklist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.databinding.FragmentTaskListBinding
import io.github.beleavemebe.inbox.ui.fragments.BaseFragment
import io.github.beleavemebe.inbox.util.log
import java.util.*

class TaskListFragment : BaseFragment(R.layout.fragment_task_list), ListUpdateCallback {
    private val viewModel: TaskListViewModel by viewModels()
    private val binding by viewBinding(FragmentTaskListBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.initAddButton()
        binding.setupRecyclerView()
        observeTaskList()
    }

    private fun FragmentTaskListBinding.initAddButton() {
        fabAddTask.setOnClickListener {
            goToNewTask()
        }
    }

    private fun FragmentTaskListBinding.setupRecyclerView() {
        tasksRv.let { rv ->
            rv.adapter = TaskAdapter(this@TaskListFragment, ::goToTask)
            rv.layoutManager = LinearLayoutManager(context)
            ItemTouchHelper(taskTouchHelperCallback).attachToRecyclerView(rv)
        }
    }

    private fun observeTaskList() {
        viewModel.tasks.observe(viewLifecycleOwner) { taskList ->
            log("got list of ${taskList.size} tasks: ${taskList.joinToString { it.title }}")
            val adapter = binding.tasksRv.adapter as TaskAdapter
            adapter.setContent(taskList.toList())
        }
    }

    private fun showUndoSnackbar(onUndoCallback: () -> Unit) {
        Snackbar.make(
            binding.fabAddTask,
            getString(R.string.task_removed),
            Snackbar.LENGTH_LONG
        )
            .setAction(getString(R.string.undo)) { onUndoCallback() }
            .show()
    }

    private fun deleteTask(holder: TaskViewHolder) {
        val task = holder.task ?: return
        viewModel.deleteTask(task)
        showUndoSnackbar {
            viewModel.insertTask(task)
        }
    }

    private fun goToNewTask() {
        findNavController().navigate(
            TaskListFragmentDirections
                .actionTaskListFragmentToTaskFragment(null, getString(R.string.new_task))
        )
    }

    private fun goToTask(uuid: UUID) {
        requireView().findNavController()
            .navigate(
                TaskListFragmentDirections.actionTaskListFragmentToTaskFragment(
                    uuid, getString(R.string.task)
                )
            )
    }

    private val taskTouchHelperCallback =
        object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) = deleteTask(viewHolder as TaskViewHolder)
        }

    override fun onInserted(position: Int, count: Int) {
        binding.tasksRv.scrollToPosition(position)
    }

    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
