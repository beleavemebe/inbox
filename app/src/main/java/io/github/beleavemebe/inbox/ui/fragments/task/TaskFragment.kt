package io.github.beleavemebe.inbox.ui.fragments.task

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.databinding.FragmentTaskBinding
import io.github.beleavemebe.inbox.model.Task
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*

const val TAG = "TaskFragment"

class TaskFragment : Fragment(R.layout.fragment_task) {
    private lateinit var task : Task
    private val taskViewModel by lazy { ViewModelProvider(this).get(TaskViewModel::class.java) }

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val taskId : UUID? = arguments?.get("taskId") as? UUID
        Log.d(TAG, "got task id - $taskId")
        if (taskId == null) {
            task = Task()
            taskViewModel.onNoTaskIdGiven()
        } else {
            taskViewModel.loadTask(taskId)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTaskBinding.bind(view)

        setupUI()
        setTaskLiveDataObserver()
    }

    private fun setTaskLiveDataObserver() {
        taskViewModel.taskLiveData.observe(viewLifecycleOwner) { task : Task? ->
            this.task = task ?: throw IllegalArgumentException("Impossible wrong id")
            updateUI(task)
        }
    }

    private fun setupUI() {
        addTextWatchers()
        addListeners()
        addCheckboxListener()
        disableCheckbox()
        hideTimestamp()
        setHeaderText(R.string.new_task)
    }

    private fun updateUI(task: Task) = with (binding) {
        titleTi.setText(task.title)
        taskNoteTi.setText(task.note)
        taskTimestampTv.apply {
            text = getString(R.string.task_timestamp, getFormattedTimestamp())
            visibility = View.VISIBLE
        }
        doneCb.apply {
            isChecked = task.isCompleted
            jumpDrawablesToCurrentState()
        }
        setHeaderText(R.string.task)
    }


    private fun setHeaderText(@StringRes res : Int) = with (binding) {
        taskHeaderTv.text = getString(res)
    }

    private fun getFormattedTimestamp() =
        SimpleDateFormat("MMM dd `yy HH:mm", Locale.US).format(task.timestamp)

    private fun saveTask(view: View) {
        if (task.isBlank()) {
            Toast.makeText(
                context,
                getString(R.string.task_is_blank),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            taskViewModel.onExitFragment(task)
            navToTaskListFragment(view)
        }
    }

    private fun navToTaskListFragment(view: View) {
        Log.d(TAG, "navigating back from task $task")
        Navigation.findNavController(view)
            .navigate(
                R.id.action_taskFragment_to_taskListFragment
            )
    }

    private fun addTextWatchers() = with (binding) {
        TextWatcherImpl.newWatcher { sequence ->
            task.title = sequence.toString()
        }.also { titleTi.addTextChangedListener(it) }

        TextWatcherImpl.newWatcher { sequence ->
            task.note = sequence.toString()
        }.also { taskNoteTi.addTextChangedListener(it) }
    }

    private fun addListeners() = with (binding) {
        backIb.setOnClickListener(::navToTaskListFragment)
        saveIb.setOnClickListener(::saveTask)
    }

    private fun addCheckboxListener() = with (binding) {
        doneCb.setOnCheckedChangeListener { _, isChecked ->
            Log.d(TAG, "checkbox pressed")
            task.isCompleted = isChecked
        }
    }

    private fun disableCheckbox() = with (binding) {
        doneCb.isEnabled = false
    }

    private fun hideTimestamp() = with (binding) {
        taskTimestampTv.visibility = View.INVISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun Task.isBlank() : Boolean {
        return title == ""
    }

    private class TextWatcherImpl(private val onTextChangedAction: (CharSequence) -> Unit) : TextWatcher {
        companion object {
            fun newWatcher(onTextChangedAction: (CharSequence) -> Unit) =
                TextWatcherImpl(onTextChangedAction)
        }

        override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) =
            onTextChangedAction.invoke(sequence ?: "")

        override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(sequence: Editable?) {}
    } // lord forgive me
}
