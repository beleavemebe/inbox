package io.github.beleavemebe.inbox.data.db

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.github.beleavemebe.inbox.domain.model.ChecklistItem
import java.util.*
import javax.inject.Inject

@ProvidedTypeConverter
class Converters @Inject constructor(
    private val moshi: Moshi,
) {
    @TypeConverter
    fun fromUUID(uuid: UUID?): String =
        uuid?.toString() ?: ""

    @TypeConverter
    fun toUUID(id: String?): UUID =
        UUID.fromString(id)

    @TypeConverter
    fun fromDate(date: Date?): Long? =
        date?.time

    @TypeConverter
    fun toDate(ms: Long?): Date? =
        ms?.let { Date(it) }

    private val checklistItemsType =
        Types.newParameterizedType(List::class.java, ChecklistItem::class.java)

    @TypeConverter
    fun fromChecklistItems(items: List<ChecklistItem>?): String {
        return moshi
            .adapter<List<ChecklistItem>>(checklistItemsType)
            .toJson(items)
    }

    @TypeConverter
    fun toChecklistItems(json: String?): List<ChecklistItem>? {
        return moshi
            .adapter<List<ChecklistItem>>(checklistItemsType)
            .fromJson(
                json ?: return emptyList()
            )
    }
}
