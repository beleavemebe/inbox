package io.github.beleavemebe.inbox.ui.infolist

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.databinding.FragmentInfoListBinding
import io.github.beleavemebe.inbox.ui.BaseFragment

class InfoListFragment : BaseFragment(R.layout.fragment_info_list) {
    private val viewModel: InfoListViewModel by viewModels()
    private val binding by viewBinding(FragmentInfoListBinding::bind)
}
