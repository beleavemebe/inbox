package io.github.beleavemebe.inbox.tasks.ui.task_list

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import io.github.beleavemebe.inbox.core.di.findDependencies
import io.github.beleavemebe.inbox.core.navigation.InboxFeature
import io.github.beleavemebe.inbox.core.navigation.crossFeatureNavigator
import io.github.beleavemebe.inbox.core.ui.toolbar
import io.github.beleavemebe.inbox.core.utils.repeatWhenStarted
import io.github.beleavemebe.inbox.tasks.domain.model.Task
import io.github.beleavemebe.inbox.tasks.ui.task_list.databinding.FragmentTaskListBinding
import io.github.beleavemebe.inbox.tasks.ui.task_list.di.DaggerTaskListComponent
import kotlinx.coroutines.flow.onEach
import java.util.*
import javax.inject.Inject

class TaskListFragment : Fragment(R.layout.fragment_task_list), MenuProvider, ListUpdateCallback {
    private val binding by viewBinding(FragmentTaskListBinding::bind)

    @Inject lateinit var factory: ViewModelProvider.Factory
    private val viewModel: TaskListViewModel by navGraphViewModels(R.id.nav_graph) { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerTaskListComponent.factory()
            .create(findDependencies())
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOptionsMenu()
        initAddButton()
        initRecyclerView()
        observeTaskList()
        observeActionBarSubtitle()
    }

    private fun initOptionsMenu() {
        (requireActivity() as MenuHost)
            .addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_task_list, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.filter_tasks -> {
                showFilterTasksDialog()
                true
            }
            else -> false
        }
    }

    private fun initAddButton() {
        binding.fabAddTask.setOnClickListener { goToNewTask() }
    }

    private fun initRecyclerView() {
        binding.tasksRv.adapter = TaskAdapter(this, ::goToTask, viewModel::setTaskCompleted)
        binding.tasksRv.layoutManager = LinearLayoutManager(context)
        ItemTouchHelper(taskTouchHelperCallback).attachToRecyclerView(binding.tasksRv)
    }

    private fun observeTaskList() {
        viewModel.tasks
            .onEach { updateTaskList(it) }
            .repeatWhenStarted(viewLifecycleOwner.lifecycle)
    }

    private fun updateTaskList(taskList: List<Task>) {
        val adapter = binding.tasksRv.adapter as TaskAdapter
        adapter.setContent(taskList.toList())
    }

    private fun observeActionBarSubtitle() {
        viewModel.taskFilterPreference
            .onEach { updateActionBarSubtitle(it) }
            .repeatWhenStarted(viewLifecycleOwner.lifecycle)
    }

    private fun updateActionBarSubtitle(pref: TaskFilterPreference) {
        toolbar.subtitle = when (pref) {
            TaskFilterPreference.UNFILTERED -> ""
            else -> getString(pref.titleResId)
        }
    }

    private fun goToNewTask() {
        crossFeatureNavigator.navigateToFeature(InboxFeature.TaskDetails(null, getString(R.string.new_task)))
    }

    private fun goToTask(uuid: UUID) {
        crossFeatureNavigator.navigateToFeature(InboxFeature.TaskDetails(uuid, getString(R.string.task)))
    }

    private fun showFilterTasksDialog() {
        findNavController().navigate(
            TaskListFragmentDirections.actionTaskListFragmentToFilterTasksDialog()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        toolbar.subtitle = ""
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

    private fun deleteTask(holder: TaskViewHolder) {
        val task = holder.task ?: return
        viewModel.deleteTask(task)
        showUndoDeletionSnackbar(task)
    }

    private fun showUndoDeletionSnackbar(task: Task) {
        Snackbar.make(
            binding.fabAddTask,
            getString(R.string.task_removed),
            Snackbar.LENGTH_LONG
        )
            .setAction(getString(R.string.undo)) { viewModel.insertTask(task) }
            .show()
    }

    override fun onInserted(position: Int, count: Int) {
        binding.tasksRv.scrollToPosition(position)
    }

    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
