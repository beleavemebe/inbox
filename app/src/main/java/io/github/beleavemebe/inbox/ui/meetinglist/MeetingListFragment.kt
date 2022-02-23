package io.github.beleavemebe.inbox.ui.meetinglist

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.databinding.FragmentMeetingListBinding
import io.github.beleavemebe.inbox.ui.BaseFragment

class MeetingListFragment :
    BaseFragment(R.layout.fragment_meeting_list)
{
    private val viewModel: MeetingListViewModel by viewModels()
    private val binding by viewBinding(FragmentMeetingListBinding::bind)
}
