package io.github.beleavemebe.inbox.ui.fragments.tasklist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.databinding.FragmentTaskListBinding
import io.github.beleavemebe.inbox.ui.adapters.TaskAdapter

class TaskListFragment : Fragment(R.layout.fragment_task_list) {
    private val taskListViewModel by lazy { ViewModelProvider(this).get(TaskListViewModel::class.java) }

    private var _binding : FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTaskListBinding.bind(view)

        setupRecyclerView()
        initAddButton()
        setLiveDataObserver()
    }

    private fun setupRecyclerView() = with (binding.tasksRv) {
        layoutManager = LinearLayoutManager(context)
        adapter = TaskAdapter()
    }

    private fun initAddButton() = with (binding.addTaskFab) {
        setOnClickListener {
            navToTaskFragment()
        }
    }

    private fun setLiveDataObserver() {
        taskListViewModel.taskListLiveData.observe(viewLifecycleOwner) { taskList ->
            val adapter = binding.tasksRv.adapter as TaskAdapter
            adapter.submitList(taskList)
        }
    }

    private fun navToTaskFragment() {
        findNavController().navigate(
            TaskListFragmentDirections.actionTaskListFragmentToTaskFragment(null)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}