package io.github.beleavemebe.inbox.ui.fragments.tasklist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.databinding.FragmentTaskListBinding
import io.github.beleavemebe.inbox.repositories.TaskRepository
import io.github.beleavemebe.inbox.ui.adapters.TaskAdapter
import io.github.beleavemebe.inbox.ui.adapters.TaskViewHolder

class TaskListFragment : Fragment(R.layout.fragment_task_list) {
    private val repo get() = TaskRepository.getInstance()
    private val taskListViewModel: TaskListViewModel by viewModels()
    private val binding : FragmentTaskListBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAddButton()
        setupRecyclerView()
        setTaskListLiveDataObserver()
    }

    private fun initAddButton() = with (binding.addTaskFab) {
        setOnClickListener { navToTaskFragment() }
    }

    private fun setupRecyclerView() = with (binding.tasksRv) {
        layoutManager = LinearLayoutManager(context)
        adapter = TaskAdapter()
        ItemTouchHelper(touchHelperCallback).attachToRecyclerView(this)
    }

    private fun setTaskListLiveDataObserver() {
        taskListViewModel.taskListLiveData.observe(viewLifecycleOwner) { taskList ->
            val adapter = binding.tasksRv.adapter as TaskAdapter
            adapter.submitList(taskList)
        }
    }

    private val touchHelperCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
    {
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

    private fun deleteTask(holder: TaskViewHolder) = repo.deleteTask(holder.task)

    private fun navToTaskFragment() {
        findNavController().navigate(
            TaskListFragmentDirections.actionTaskListFragmentToTaskFragment(null)
        )
    }
}
