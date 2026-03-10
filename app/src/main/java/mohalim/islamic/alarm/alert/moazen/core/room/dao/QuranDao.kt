package mohalim.islamic.alarm.alert.moazen.core.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mohalim.islamic.alarm.alert.moazen.core.room.entity.QuranPageEntity

@Dao
interface QuranDao {
    @Query("SELECT * FROM quran_pages WHERE pageNumber = :pageNumber")
    suspend fun getPage(pageNumber: Int): QuranPageEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPage(page: QuranPageEntity)

    @Query("SELECT COUNT(*) FROM quran_pages")
    suspend fun getPagesCount(): Int
}
