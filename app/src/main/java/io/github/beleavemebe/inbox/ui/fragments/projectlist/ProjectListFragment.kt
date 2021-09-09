package io.github.beleavemebe.inbox.ui.fragments.projectlist

import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import io.github.beleavemebe.inbox.R

class ProjectListFragment : Fragment(R.layout.fragment_project_list) {
    private val projectListViewModel: ProjectListViewModel by viewModels()
}