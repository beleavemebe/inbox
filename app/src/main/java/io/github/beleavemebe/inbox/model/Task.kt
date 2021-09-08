package io.github.beleavemebe.inbox.model

import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Task(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val timestamp: Date = Date(),
    var title: String = "",
    @ColumnInfo(name = "is_completed") var isCompleted: Boolean = false,
    var note: String? = null,
    var deadline: Date? = null,
    @Embedded(prefix = "rep_freq") var repeatFrequency: TaskRepeatFrequency? = null,
    var repetitionReferenceTimestamp: Date? = null
) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean =
                oldItem == newItem
        }
    }
}