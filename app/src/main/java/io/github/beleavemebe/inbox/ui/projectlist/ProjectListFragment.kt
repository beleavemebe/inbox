package io.github.beleavemebe.inbox.ui.projectlist

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.databinding.FragmentProjectListBinding

class ProjectListFragment : Fragment(R.layout.fragment_project_list) {
    private val viewModel: ProjectListViewModel by viewModels()
    private val binding by viewBinding(FragmentProjectListBinding::bind)
}
