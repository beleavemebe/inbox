package io.github.beleavemebe.inbox.ui.fragments.meetinglist

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.github.beleavemebe.inbox.R

class MeetingListFragment : Fragment(R.layout.fragment_meeting_list) {
    private val meetingListViewModel by lazy { ViewModelProvider(this).get(MeetingListViewModel::class.java) }
}