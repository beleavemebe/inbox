package io.github.beleavemebe.inbox.ui.fragments.projectlist

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.databinding.FragmentProjectListBinding
import io.github.beleavemebe.inbox.ui.fragments.BaseFragment
import io.github.beleavemebe.inbox.util.mainToolbar

class ProjectListFragment :
    BaseFragment(R.layout.fragment_project_list)
{
    private val viewModel: ProjectListViewModel by viewModels()
    private val binding by viewBinding(FragmentProjectListBinding::bind)

    override fun adaptMainToolbar() {
        with(mainToolbar) {
            title = getString(R.string.my_projects)
        }
    }
}
