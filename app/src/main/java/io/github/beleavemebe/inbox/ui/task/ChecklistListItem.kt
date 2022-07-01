package io.github.beleavemebe.inbox.ui.task

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import io.github.beleavemebe.inbox.domain.model.ChecklistItem
import io.github.beleavemebe.inbox.databinding.ListItemAddChecklistEntryBinding
import io.github.beleavemebe.inbox.databinding.ListItemChecklistEntryBinding
import io.github.beleavemebe.inbox.ui.util.enableDoneImeAction

sealed class ChecklistListItem {
    data class ChecklistEntry(val checklistItem: ChecklistItem) : ChecklistListItem() {
        companion object {
            @JvmStatic
            private fun inflateBinding(
                inflater: LayoutInflater,
                container: ViewGroup,
            ) = ListItemChecklistEntryBinding.inflate(inflater, container, false)

            fun delegate(
                onTextChanged: (Int, String) -> Unit,
                onItemChecked: (Int, Boolean) -> Unit,
            ) =
                adapterDelegateViewBinding<ChecklistEntry, ChecklistListItem, ListItemChecklistEntryBinding>(
                    ::inflateBinding,
                ) {
                    bind {
                        binding.cbCompleted.isChecked = item.checklistItem.isDone
                        binding.etChecklistItemText.setText(item.checklistItem.text)
                        binding.etChecklistItemText.enableDoneImeAction()
                        binding.etChecklistItemText.doOnTextChanged { text, _, _, _ ->
                            onTextChanged(absoluteAdapterPosition, text.toString())
                        }
                        binding.cbCompleted.setOnCheckedChangeListener { _, isChecked ->
                            onItemChecked(absoluteAdapterPosition, isChecked)
                        }
                    }
                }
        }
    }

    class AddChecklistEntry : ChecklistListItem() {
        companion object {
            @JvmStatic
            private fun inflateBinding(
                inflater: LayoutInflater,
                container: ViewGroup,
            ) = ListItemAddChecklistEntryBinding.inflate(inflater, container, false)

            fun delegate(onClick: (Context) -> Unit) =
                adapterDelegateViewBinding<AddChecklistEntry, ChecklistListItem, ListItemAddChecklistEntryBinding>(
                    ::inflateBinding,
                ) {
                    bind {
                        binding.root.setOnClickListener {
                            onClick(context.applicationContext)
                        }
                    }
                }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ChecklistListItem>() {
            override fun areItemsTheSame(
                oldItem: ChecklistListItem,
                newItem: ChecklistListItem
            ): Boolean {
                return when (oldItem) {
                    is ChecklistEntry -> newItem is ChecklistEntry && oldItem.checklistItem === newItem.checklistItem
                    is AddChecklistEntry -> newItem is AddChecklistEntry && oldItem === newItem
                }
            }

            override fun areContentsTheSame(
                oldItem: ChecklistListItem,
                newItem: ChecklistListItem
            ): Boolean {
                return newItem == oldItem
            }
        }
    }
}
