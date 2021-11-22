package io.github.beleavemebe.inbox.ui.fragments.infolist

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.databinding.FragmentInfoListBinding
import io.github.beleavemebe.inbox.ui.fragments.BaseFragment
import io.github.beleavemebe.inbox.util.mainToolbar

class InfoListFragment :
    BaseFragment(R.layout.fragment_info_list)
{
    private val viewModel: InfoListViewModel by viewModels()
    private val binding by viewBinding(FragmentInfoListBinding::bind)

    override fun adaptMainToolbar() {
        with(mainToolbar) {
            title = getString(R.string.information)
        }
    }
}
