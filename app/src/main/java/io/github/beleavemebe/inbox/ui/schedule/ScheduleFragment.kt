package io.github.beleavemebe.inbox.ui.schedule

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.databinding.FragmentScheduleBinding
import io.github.beleavemebe.inbox.ui.BaseFragment

class ScheduleFragment :
    BaseFragment(R.layout.fragment_schedule)
{
    private val viewModel: ScheduleViewModel by viewModels()
    private val binding by viewBinding(FragmentScheduleBinding::bind)
}
