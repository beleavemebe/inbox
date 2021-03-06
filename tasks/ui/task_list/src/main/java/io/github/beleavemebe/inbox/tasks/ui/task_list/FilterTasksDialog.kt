package io.github.beleavemebe.inbox.tasks.ui.task_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navGraphViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.beleavemebe.inbox.tasks.ui.task_list.databinding.DialogFilterTasksBinding

class FilterTasksDialog : BottomSheetDialogFragment() {
    private val viewModel by navGraphViewModels<TaskListViewModel>(R.id.nav_graph)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DialogFilterTasksBinding.bind(
            inflater.inflate(
                R.layout.dialog_filter_tasks,
                container,
                false
            )
        )

        initRadioGroup(binding)
        initRadioGroupListener(binding)

        return binding.root
    }

    private fun initRadioGroup(binding: DialogFilterTasksBinding) {
        val correspondingRb = when (viewModel.taskFilterPreference.value) {
            TaskFilterPreference.UNFILTERED -> binding.unsortedRb
            TaskFilterPreference.UNDATED -> binding.undatedRb
            TaskFilterPreference.DUE_THIS_WEEK -> binding.thisWeekRb
            TaskFilterPreference.DUE_THIS_OR_NEXT_WEEK -> binding.thisOrNextWeekRb
        }

        correspondingRb.isChecked = true
    }

    private fun initRadioGroupListener(binding: DialogFilterTasksBinding) {
        binding.sortPreferencesRg.setOnCheckedChangeListener { _, checkedId ->
            val preference = when (checkedId) {
                R.id.unsorted_rb -> TaskFilterPreference.UNFILTERED
                R.id.undated_rb -> TaskFilterPreference.UNDATED
                R.id.this_week_rb -> TaskFilterPreference.DUE_THIS_WEEK
                R.id.this_or_next_week_rb -> TaskFilterPreference.DUE_THIS_OR_NEXT_WEEK
                else -> error("Unknown RadioButton id")
            }

            viewModel.onPreferenceSelected(preference)
            dismiss()
        }
    }
}
