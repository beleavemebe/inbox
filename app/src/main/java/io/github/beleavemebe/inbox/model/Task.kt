package io.github.beleavemebe.inbox.model

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
    @ColumnInfo(name="date")               var date: Date? = null,
    @ColumnInfo(name="is_time_specified")  var isTimeSpecified: Boolean? = null,
)
