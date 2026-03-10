package mohalim.islamic.alarm.alert.moazen.core.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quran_pages")
data class QuranPageEntity(
    @PrimaryKey val pageNumber: Int,
    val data: String // JSON string of PageApi
)
