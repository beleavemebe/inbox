package io.github.beleavemebe.inbox.model

import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Task(
    @PrimaryKey @ColumnInfo(name="id")     val id: UUID = UUID.randomUUID(),
    @ColumnInfo(name="timestamp")          val timestamp: Date = Date(),
    @ColumnInfo(name="title")              var title: String = "",
    @ColumnInfo(name="is_completed")       var isCompleted: Boolean = false,
    @ColumnInfo(name="note")               var note: String? = null,
    @ColumnInfo(name="deadline")           var deadline: Date? = null,
    @Embedded(prefix="rep_freq")           var repeatFrequency: TaskRepeatFrequency? = null,
    @ColumnInfo(name="rep_ref_timestamp")  var repetitionReferenceTimestamp: Date? = null,
    @ColumnInfo(name="date")               var date: Date ?= null,
    @ColumnInfo(name="duration")           var durationMs: Long ?= null,
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