package io.github.beleavemebe.inbox.ui.fragments.projectlist

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.databinding.FragmentProjectListBinding
import io.github.beleavemebe.inbox.ui.activities.MainActivity.Companion.mainToolbar
import io.github.beleavemebe.inbox.ui.fragments.BaseFragment

class ProjectListFragment :
    BaseFragment(R.layout.fragment_project_list)
{
    private val viewModel: ProjectListViewModel by viewModels()
    private val binding by viewBinding(FragmentProjectListBinding::bind)
}
