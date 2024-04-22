package mohalim.islamic.alarm.alert.moazen.core.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "azkar")
data class AzkarEntity(
    @PrimaryKey var id: Int?,
    @ColumnInfo(name = "zekr_string") var zekrString: String,
    @ColumnInfo(name = "count") var count: Int = 0
)