package io.github.beleavemebe.inbox.ui.fragments.infolist

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.github.beleavemebe.inbox.R

class InfoListFragment : Fragment(R.layout.fragment_info_list) {
    private val viewModel by lazy { ViewModelProvider(this).get(InfoListViewModel::class.java) }
}