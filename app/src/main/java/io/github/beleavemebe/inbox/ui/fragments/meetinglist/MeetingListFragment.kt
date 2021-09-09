package io.github.beleavemebe.inbox.ui.fragments.meetinglist

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import io.github.beleavemebe.inbox.R

class MeetingListFragment : Fragment(R.layout.fragment_meeting_list) {
    private val meetingListViewModel: MeetingListViewModel by viewModels()
}