package io.github.beleavemebe.inbox.ui.schedule

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.databinding.FragmentScheduleBinding

class ScheduleFragment : Fragment(R.layout.fragment_schedule) {
    private val viewModel: ScheduleViewModel by viewModels()
    private val binding by viewBinding(FragmentScheduleBinding::bind)
}
