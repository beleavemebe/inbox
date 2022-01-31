package io.github.beleavemebe.inbox.data.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.*

@TypeConverters
class TaskTypeConverters {
    @TypeConverter
    fun fromUUID(uuid: UUID?) : String =
        uuid?.toString() ?: ""

    @TypeConverter
    fun toUUID(id: String?) : UUID =
        UUID.fromString(id)

    @TypeConverter
    fun fromDate(date: Date?) : Long? =
        date?.time

    @TypeConverter
    fun toDate(ms : Long?) : Date? =
        ms?.let { Date(it) }
}
