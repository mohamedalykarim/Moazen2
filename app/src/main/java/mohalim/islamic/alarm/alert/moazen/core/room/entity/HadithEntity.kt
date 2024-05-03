package mohalim.islamic.alarm.alert.moazen.core.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hadith")
data class HadithEntity(
    @PrimaryKey
    val number : Int,
    @ColumnInfo(name = "hadith")
    val hadith : String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "searchTerm")
    val searchTerm: String,
    @ColumnInfo(name = "rawy")
    var rawy : String = ""
)
