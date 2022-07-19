package io.github.beleavemebe.inbox.tasks.ui.task_details

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import io.github.beleavemebe.inbox.core.utils.enableDoneImeAction
import io.github.beleavemebe.inbox.core.utils.getColorCompat
import io.github.beleavemebe.inbox.core.utils.setCrossedOut
import io.github.beleavemebe.inbox.tasks.domain.model.ChecklistItem
import io.github.beleavemebe.inbox.tasks.ui.task_details.databinding.ListItemAddChecklistEntryBinding
import io.github.beleavemebe.inbox.tasks.ui.task_details.databinding.ListItemChecklistEntryBinding

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
                        val isItemDone = item.checklistItem.isDone
                        val res = binding.root.resources

                        binding.cbCompleted.isChecked = isItemDone
                        binding.cbCompleted.setOnCheckedChangeListener { _, isChecked ->
                            onItemChecked(adapterPosition, isChecked)
                        }

                        binding.etChecklistItemText.isClickable = !isItemDone
                        binding.etChecklistItemText.isFocusable = !isItemDone
                        binding.etChecklistItemText.setCrossedOut(isItemDone)
                        binding.etChecklistItemText.enableDoneImeAction()
                        binding.etChecklistItemText.setText(item.checklistItem.text)
                        binding.etChecklistItemText.doOnTextChanged { text, _, _, _ ->
                            onTextChanged(adapterPosition, text.toString())
                        }
                        binding.etChecklistItemText.setCrossedOut(item.checklistItem.isDone)
                        binding.etChecklistItemText.setTextColor(
                            res.getColorCompat(
                                context,
                                if (item.checklistItem.isDone) {
                                    R.color.task_item_title_completed
                                } else {
                                    R.color.task_item_title
                                }
                            )
                        )
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
                newItem: ChecklistListItem,
            ): Boolean {
                return when (oldItem) {
                    is ChecklistEntry -> newItem is ChecklistEntry && oldItem.checklistItem === newItem.checklistItem
                    is AddChecklistEntry -> newItem is AddChecklistEntry && oldItem === newItem
                }
            }

            override fun areContentsTheSame(
                oldItem: ChecklistListItem,
                newItem: ChecklistListItem,
            ): Boolean {
                return newItem == oldItem
            }
        }
    }
}
