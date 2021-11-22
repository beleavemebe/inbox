package io.github.beleavemebe.inbox.ui.fragments.meetinglist

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.databinding.FragmentMeetingListBinding
import io.github.beleavemebe.inbox.ui.fragments.BaseFragment
import io.github.beleavemebe.inbox.util.mainToolbar

class MeetingListFragment :
    BaseFragment(R.layout.fragment_meeting_list)
{
    private val viewModel: MeetingListViewModel by viewModels()
    private val binding by viewBinding(FragmentMeetingListBinding::bind)

    override fun adaptMainToolbar() {
        with(mainToolbar) {
            title = getString(R.string.my_meetings)
        }
    }
}
